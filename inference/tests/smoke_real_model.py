"""CPU-only smoke test: load and run each bundled crop model on one real JPG.

No Flask, database or browser is needed. This checks basic compatibility,
not prediction accuracy, safety or reliable crop-disease diagnosis.
"""
import gc
from pathlib import Path

from ultralytics import YOLO


CROPS = (
    ("apple", "苹果"),
    ("corn", "玉米"),
    ("cotton", "棉花"),
    ("grape", "葡萄"),
    ("potato", "马铃薯"),
    ("rice", "水稻"),
    ("strawberry", "草莓"),
    ("tomato", "番茄"),
    ("wheat", "小麦"),
)


def main():
    root = Path(__file__).resolve().parents[2]
    tested = 0
    for crop, chinese_folder in CROPS:
        weights = root / "inference" / "weights" / (crop + "_best.pt")
        samples = sorted((root / "assets" / "samples" / chinese_folder).glob("*.jpg"))
        if not weights.is_file() or not samples:
            raise SystemExit("Missing checkpoint or sample for {}".format(crop))

        image = samples[0]
        print("Checking {} with {} ...".format(crop, image.relative_to(root)), flush=True)
        model = YOLO(str(weights))
        results = model.predict(
            source=str(image), device="cpu", imgsz=640,
            conf=0.25, save=False, verbose=False,
        )
        assert len(results) == 1, "Expected one image result for {}".format(crop)
        result = results[0]
        assert result.boxes is not None, "Missing detection result for {}".format(crop)
        assert len(result.orig_shape) == 2 and all(n > 0 for n in result.orig_shape)
        print("OK: {} — image {} — {} detections".format(
            crop, result.orig_shape, len(result.boxes)), flush=True)
        tested += 1
        del result, results, model
        gc.collect()

    assert tested == len(CROPS)
    print("All {} crop checkpoints completed CPU inference.".format(tested))
    print("This is a smoke test only, NOT a validation of model accuracy.")


if __name__ == "__main__":
    main()
