# Contributing to CropLens

Thanks for improving CropLens. This repository contains a Vue frontend, Spring Boot API and Flask/YOLO inference service. Please prefer small, focused PRs and avoid unrelated mass formatting or moving binary models.

## Before opening a PR

1. Check existing issues and the [architecture](docs/ARCHITECTURE.md) and [configuration guide](docs/CONFIGURATION.md).
2. Fork or branch from `main`; name the branch for one change (for example, `fix/inference-result`).
3. Run relevant checks locally:
   - `cd frontend && npm ci && npm run build`
   - `cd backend && mvn -Dtest=DeepSeekControllerTest,FileControllerTest test`
   - `cd inference && python -m unittest discover -s tests -v` (standard-library tests use mocked model objects)
4. Summarize behavior changes, required configuration, screenshots where appropriate and exactly what has and has not been tested.
5. Link any relevant issue, and ensure CI passes before requesting review.

## Safety and provenance

Do not add API keys, private datasets, identifiable uploads, generated build output or model weights without verifying provenance, redistributability and size. Do not inflate model confidence or claim measured accuracy without evidence. Preserve upstream copyright and licensing for incorporated projects. See [SECURITY.md](SECURITY.md) for responsible vulnerability reporting.

**Hardware, camera, video and live-model execution require separate validation**; passing unit tests is not proof of end-to-end functionality.
