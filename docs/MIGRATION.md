# CropLens — Directory migration (2026-10)

The layout was normalized without rewriting service sources or model artifacts:

| Old path | New path |
| --- | --- |
| `YOLO_AI_CropDisease_Detection_Vue/` | `frontend/` |
| `YOLO_AI_CropDisease_Detection_SpringBoot/` | `backend/` |
| `YOLO_AI_CropDisease_Detection_Flask/` | `inference/` |
| `cropdisease.sql` | `database/schema.sql` |
| `README_zh.md` | `README_zh-CN.md` |
| Root-level legacy DeepSeek key guide | `docs/DEEPSEEK-KEYS.zh-CN.md` |

All relative paths *within* each service are left unchanged. Start the services from their new directories (`frontend`, `backend`, `inference`) so relative model paths and uploaded files still resolve as intended. The Vite local proxy defaults to Spring Boot on port 9999 and Flask on port 5000.

Existing clone users can `git pull` and update scripts and working directories. The original commits remain available in Git history. The repository itself is still under the original GitHub URL until an administrator renames it.

The existing `测试图片/` folder, tracked IDE files, model weights and uploaded media are retained for now because removing or relocating them requires separate validation and redistribution/license review. **This migration does not prove that full image/video inference works.**
