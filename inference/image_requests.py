"""Per-request image prediction orchestration for the local CropLens demo.

The video/camera routes still maintain their own shared mutable state and
need a separate concurrency review; this helper only isolates images.
"""
import json
import uuid
from pathlib import Path


def process_image(data, weight_path, threshold, predictor_class, upload, output_dir="./runs"):
    """Predict one image and return the existing JSON-compatible response shape.

    All mutable state and the rendered output filename are local to a single
    request; the temporary image is removed even if prediction/upload fails.
    """
    response = {
        "username": data.get("username"),
        "weight": data["weight"],
        "conf": data["conf"],
        "startTime": data.get("startTime"),
        "inputImg": data["inputImg"],
        "kind": data["kind"],
    }
    folder = Path(output_dir)
    folder.mkdir(parents=True, exist_ok=True)
    output = folder / ("result_" + uuid.uuid4().hex + ".jpg")
    try:
        predictor = predictor_class(
            weights_path=weight_path,
            img_path=response["inputImg"],
            save_path=str(output),
            kind=response["kind"],
            conf=threshold,
        )
        result = predictor.predict()
        if result["labels"] == "预测失败":
            response.update({"status": 400, "message": "该图片无法识别，请重新上传！"})
            return response
        if not output.is_file():
            response.update({"status": 400, "message": "预测结果图片未生成"})
            return response
        url = upload(str(output))
        if not url:
            response.update({"status": 400, "message": "预测图片上传失败"})
            return response
        response.update({
            "status": 200,
            "message": "预测成功",
            "outImg": url,
            "allTime": result["allTime"],
            "confidence": json.dumps(result["confidences"]),
            "label": json.dumps(result["labels"], ensure_ascii=False),
        })
        return response
    finally:
        if output.is_file():
            output.unlink()
