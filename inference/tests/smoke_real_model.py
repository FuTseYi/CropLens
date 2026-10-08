"""Execute one real CPU-only crop model prediction without Flask or a database.

Unlike the standard mock-based unit tests, this must load a checked-in
Ultralytics checkpoint and decode an actual sample image. It does not
evaluate diagnostic accuracy or full frontend/backend integration.
"""
from pathlib import Path
from ultralytics import YOLO


def main():
    root = Path(__file__).resolve().parents[2]
    weights = root / "inference" / "weights" / "wheat_best.pt"
    images = sorted((root / "assets" / "samples" / "小麦").glob("*.jpg"))
    if not weights.is_file() or not images:
        raise SystemExit("Required wheat checkpoint/sample not found")
    image = images[0]

    print("Model:", weights.relative_to(root))
    print("Image:", image.relative_to(root))
    model = YOLO(str(weights))
    results = model.predict(
        source=str(image),
        device="cpu",
        imgsz=640,
        conf=0.25,
        save=False,
        verbose=False,
    )
    assert len(results) == 1, "Expected exactly one image prediction"
    result = results[0]
    assert result.boxes is not None, "Expected a detection result structure"
    assert len(result.orig_shape) == 2 and all(n > 0 for n in result.orig_shape)
    print("Real inference succeeded; image shape:", result.orig_shape)
    print("Detections:", len(result.boxes))
    print("This is a smoke test, not an accuracy or clinical evaluation.")


if __name__ == "__main__":
    main()
