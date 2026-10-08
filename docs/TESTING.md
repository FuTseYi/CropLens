# CropLens — Testing matrix

| Level | Command / workflow | What it verifies | What it does not verify |
| --- | --- | --- | --- |
| Python unit tests | `cd inference && python -m unittest discover -s tests -v` | Confidence handling, model path and local URL restrictions, plus independent concurrent image request outputs using mock objects | Actual trained model or image decoding |
| Vue build | `cd frontend && npm ci && npm run build` | The frontend bundle compiles | Browser integration or API responses |
| Spring Boot checks | `cd backend && mvn -Dtest=DeepSeekControllerTest,FileControllerTest package` | Backend compilation, selected input/control tests | Database integration or paid AI connectivity |
| Documentation links | `python scripts/check_docs_links.py` | Relative Markdown links resolve | External link availability |
| **Real YOLO smoke test** | GitHub Actions: `Real YOLO CPU smoke test` | Loads an actual wheat checkpoint and predicts one bundled JPG on a GitHub-hosted CPU | Diagnostic accuracy, full application workflow, multi-user concurrency, camera/video streaming, other weights |

## Run the real inference smoke test

The workflow runs automatically on `main` when model checkpoints, its test script, or the smoke workflow are changed. It can also be triggered manually from an authenticated GitHub CLI terminal:

```powershell
gh workflow run real-yolo-smoke.yml -R FuTseYi/CropLens
gh run list -R FuTseYi/CropLens --workflow real-yolo-smoke.yml --limit 1
```

Alternatively, use **Actions → Real YOLO CPU smoke test → Run workflow** in the GitHub repository.

The optional test installs CPU PyTorch/Ultralytics packages and may download substantial dependencies. It is intentionally not run on every PR or on ordinary documentation updates. Read the run logs for the actual pass/fail status. A passing smoke test indicates only that **one checkpoint can load and process one repository sample**; it says nothing about inference accuracy or deployment readiness.

## Before claiming production readiness

The image route now uses per-request output paths and no longer mutates shared video state; unit tests simulate overlapping image calls. This does not prove end-to-end concurrency or cover the still-shared video/camera state. Run complete image/video/camera inference against the local Spring Boot upload/database flow, verify model-class mappings and nonempty/empty detections, profile CPU/GPU resource usage, test overlapping user sessions, audit upload authentication and content validation, and document the provenance/redistribution permissions of sample data and pretrained model assets.
