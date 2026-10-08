# CropLens — Real CPU inference verification

**Date:** 2026-10-08  
**GitHub Actions run:** [Real YOLO CPU smoke test #37754791593](https://github.com/FuTseYi/CropLens/actions/runs/37754791593)  
**Repository commit:** [ae959b79fd206177525b24c896f7a9d76423d852](https://github.com/FuTseYi/CropLens/commit/ae959b79fd206177525b24c896f7a9d76423d852)  
**Conclusion:** Success.

## What was actually executed

The workflow ran `inference/tests/smoke_real_model.py` with PyTorch + Ultralytics on a GitHub-hosted CPU runner. It loaded each checked-in `inference/weights/<crop>_best.pt` checkpoint and ran inference on the first matching JPG from `assets/samples/`.

| Crop | Sample category | Checkpoint | Inference |
| --- | --- | --- | --- |
| Apple | 苹果 | `apple_best.pt` | Passed |
| Corn | 玉米 | `corn_best.pt` | Passed |
| Cotton | 棉花 | `cotton_best.pt` | Passed |
| Grape | 葡萄 | `grape_best.pt` | Passed |
| Potato | 马铃薯 | `potato_best.pt` | Passed |
| Rice | 水稻 | `rice_best.pt` | Passed |
| Strawberry | 草莓 | `strawberry_best.pt` | Passed |
| Tomato | 番茄 | `tomato_best.pt` | Passed |
| Wheat | 小麦 | `wheat_best.pt` | Passed |

All nine invocations returned one decoded image result and a valid detection result structure. The execution logs report one detection per sample; these counts **do not establish whether the labels or bounding boxes were correct**.

## What this does *not* establish

- No mAP, precision, recall, robustness or statistically sound accuracy assessment was performed.
- Only one selected sample per crop was tested, not all 132 demonstration images or independent holdout datasets.
- The test does not use Flask, Spring Boot, Vue, MySQL, file uploads or DeepSeek; those full application paths remain unverified.
- Concurrent video/camera streaming, target-device runtime and third-party image/model licensing require separate review.

The latest state of the smoke workflow may differ from this historical successful run. Check [the workflow page](https://github.com/FuTseYi/CropLens/actions/workflows/real-yolo-smoke.yml) for subsequent results.
