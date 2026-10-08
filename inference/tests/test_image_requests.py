"""Regression tests for request-local image results, without torch or Flask."""
import tempfile
import unittest
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path
from threading import Barrier

from image_requests import process_image


def payload(image):
    return {
        "username": image,
        "weight": "wheat_best.pt",
        "conf": "0.5",
        "startTime": "2026-10-08",
        "inputImg": image,
        "kind": "wheat",
    }


class ImageRequestTests(unittest.TestCase):
    def setUp(self):
        self.directory = tempfile.TemporaryDirectory()
        self.addCleanup(self.directory.cleanup)
        self.root = Path(self.directory.name)

    def test_success_uses_unique_file_and_cleans_up(self):
        class Predictor:
            def __init__(self, **params):
                self.params = params

            def predict(self):
                Path(self.params["save_path"]).write_text("rendered", encoding="utf-8")
                return {"labels": ["Leaf_Rust"], "confidences": ["73.00%"], "allTime": "0.2秒"}

        captured = []

        def upload(filename):
            captured.append(Path(filename).read_text(encoding="utf-8"))
            return "http://localhost:9999/files/test.jpg"

        result = process_image(payload("image-a"), "/weights/wheat_best.pt", 0.5, Predictor, upload, self.root)
        self.assertEqual(result["status"], 200)
        self.assertEqual(result["username"], "image-a")
        self.assertEqual(result["outImg"], "http://localhost:9999/files/test.jpg")
        self.assertEqual(result["confidence"], '["73.00%"]')
        self.assertEqual(captured, ["rendered"])
        self.assertEqual(list(self.root.iterdir()), [])

    def test_no_detection_does_not_upload(self):
        class Predictor:
            def __init__(self, **kwargs):
                pass

            def predict(self):
                return {"labels": "预测失败", "confidences": "0.00%", "allTime": "0.1秒"}

        result = process_image(
            payload("empty"), "/weights/wheat_best.pt", 0.5, Predictor,
            lambda _: self.fail("should not upload failed predictions"), self.root
        )
        self.assertEqual(result["status"], 400)
        self.assertEqual(list(self.root.iterdir()), [])

    def test_failed_upload_removes_rendered_file(self):
        class Predictor:
            def __init__(self, **kwargs):
                self.file = kwargs["save_path"]

            def predict(self):
                Path(self.file).write_bytes(b"image")
                return {"labels": ["Leaf_Rust"], "confidences": ["70.00%"], "allTime": "0.1秒"}

        result = process_image(payload("x"), "model.pt", 0.5, Predictor, lambda _: None, self.root)
        self.assertEqual(result["status"], 400)
        self.assertEqual(list(self.root.iterdir()), [])

    def test_parallel_requests_do_not_overwrite_each_other(self):
        barrier = Barrier(2)

        class Predictor:
            def __init__(self, **kwargs):
                self.filename = kwargs["save_path"]
                self.image = kwargs["img_path"]

            def predict(self):
                Path(self.filename).write_text(self.image, encoding="utf-8")
                barrier.wait(timeout=10)
                return {"labels": [self.image], "confidences": ["75.00%"], "allTime": "0.1秒"}

        def upload(filename):
            return Path(filename).read_text(encoding="utf-8")

        def predict(label):
            return process_image(payload(label), "model.pt", 0.5, Predictor, upload, self.root)

        with ThreadPoolExecutor(max_workers=2) as pool:
            results = list(pool.map(predict, ["first", "second"]))
        self.assertEqual([r["outImg"] for r in results], ["first", "second"])
        self.assertEqual([r["username"] for r in results], ["first", "second"])
        self.assertEqual([r["status"] for r in results], [200, 200])
        self.assertEqual(list(self.root.iterdir()), [])


if __name__ == "__main__":
    unittest.main()
