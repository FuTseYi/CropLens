"""Pure-stdlib regression tests for CropLens prediction reporting.

Loads predictImg.py with a fake Ultralytics module, so neither torch nor
model weights are needed to test threshold forwarding and display accuracy.
"""
import importlib.util
import sys
import types
import unittest
from pathlib import Path
from unittest.mock import patch


class FakeTensor(list):
    def numel(self):
        return len(self)


class FakeResult:
    def __init__(self):
        self.boxes = types.SimpleNamespace(
            conf=FakeTensor([0.72]), cls=FakeTensor([0])
        )
        self.names = {}
        self.saved = False

    def save(self, **kwargs):
        self.saved = True


class FakeYOLO:
    def __init__(self, weights_path):
        self.weights_path = weights_path
        self.result = FakeResult()
        self.kwargs = None

    def __call__(self, **kwargs):
        self.kwargs = kwargs
        return [self.result]


def load_predictor():
    import_path = Path(__file__).resolve().parents[1] / "predict" / "predictImg.py"
    fake_ultralytics = types.ModuleType("ultralytics")
    fake_ultralytics.YOLO = FakeYOLO
    spec = importlib.util.spec_from_file_location("croplens_predict_img", import_path)
    module = importlib.util.module_from_spec(spec)
    with patch.dict(sys.modules, {"ultralytics": fake_ultralytics}):
        spec.loader.exec_module(module)
    return module.ImagePredictor


class PredictionConfidenceTests(unittest.TestCase):
    def test_threshold_is_not_replaced_by_constant(self):
        predictor = load_predictor()(
            "weights/potato_best.pt", "example.jpg", "potato", conf=0.65
        )
        result = predictor.predict()
        self.assertAlmostEqual(predictor.model.kwargs["conf"], 0.65)
        self.assertEqual(result["labels"], ["Early_Blight(早疫病)"])

    def test_result_reports_original_yolo_confidence(self):
        predictor = load_predictor()(
            "weights/potato_best.pt", "example.jpg", "potato", conf=0.5
        )
        result = predictor.predict()
        self.assertEqual(result["confidences"], ["72.00%"])
        self.assertTrue(predictor.model.result.saved)

    def test_threshold_is_clamped_to_valid_range(self):
        Predictor = load_predictor()
        self.assertEqual(Predictor("m.pt", "a.jpg", "potato", conf=1.25).conf, 1.0)
        self.assertEqual(Predictor("m.pt", "a.jpg", "potato", conf=-0.2).conf, 0.0)


if __name__ == "__main__":
    unittest.main()
