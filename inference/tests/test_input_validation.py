"""Pure-stdlib tests for local inference trust boundaries."""
import tempfile
import unittest
from pathlib import Path

from input_validation import resolve_weight_file, validate_local_media_url


class CheckpointValidationTests(unittest.TestCase):
    def setUp(self):
        self.directory = tempfile.TemporaryDirectory()
        self.addCleanup(self.directory.cleanup)
        self.weights = Path(self.directory.name)
        (self.weights / "rice_best.pt").write_bytes(b"test fixture only")

    def test_existing_checkpoint_in_weights_directory(self):
        actual = resolve_weight_file("rice_best.pt", self.weights)
        self.assertEqual(Path(actual), (self.weights / "rice_best.pt").resolve())

    def test_rejects_missing_or_wrong_extension(self):
        for value in ("missing.pt", "rice_best.pth", "", None):
            with self.subTest(value=value), self.assertRaises(ValueError):
                resolve_weight_file(value, self.weights)

    def test_rejects_traversal_and_injected_paths(self):
        for value in ("../rice_best.pt", "subdir/rice_best.pt", "/tmp/model.pt",
                      "..\\rice_best.pt", "rice_best.pt?x=1"):
            with self.subTest(value=value), self.assertRaises(ValueError):
                resolve_weight_file(value, self.weights)

    def test_rejects_symlink_escaping_weights(self):
        outside_dir = tempfile.TemporaryDirectory()
        self.addCleanup(outside_dir.cleanup)
        outside = Path(outside_dir.name) / "real-model.pt"
        outside.write_bytes(b"outside weights directory")
        try:
            (self.weights / "external.pt").symlink_to(outside)
        except (OSError, NotImplementedError):
            self.skipTest("Symlinks unsupported")
        with self.assertRaises(ValueError):
            resolve_weight_file("external.pt", self.weights)


class UploadUrlValidationTests(unittest.TestCase):
    def test_local_upload_urls_are_accepted(self):
        for url in ("http://localhost:9999/files/abc_result.jpg",
                    "http://127.0.0.1:9999/files/123%20test.png"):
            with self.subTest(url=url):
                self.assertEqual(validate_local_media_url(url), url)

    def test_rejects_remote_or_network_file_urls(self):
        invalid = [
            "https://example.org/img.jpg",
            "http://example.org:9999/files/abc.jpg",
            "http://127.0.0.2:9999/files/abc.jpg",
            "http://localhost:5000/files/abc.jpg",
            "file:///etc/passwd",
            "http://localhost:9999@evil.test/files/abc.jpg",
        ]
        for url in invalid:
            with self.subTest(url=url), self.assertRaises(ValueError):
                validate_local_media_url(url)

    def test_rejects_paths_with_traversal_or_url_mutation(self):
        invalid = [
            "http://localhost:9999/files/../private",
            "http://localhost:9999/files/%2e%2e%2fprivate",
            "http://localhost:9999/files/%5csecret",
            "http://localhost:9999/files/",
            "http://localhost:9999/files/image.jpg?redirect=1",
            "http://localhost:9999/files/image.jpg#fragment",
            "http://localhost:9999/files/%252fsecret",
            "",
            None,
        ]
        for url in invalid:
            with self.subTest(url=url), self.assertRaises(ValueError):
                validate_local_media_url(url)


if __name__ == "__main__":
    unittest.main()
