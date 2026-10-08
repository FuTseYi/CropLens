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

Existing clone users can `git pull` and update scripts and working directories. The original commits remain available in Git history. The GitHub repository has since been renamed to [FuTseYi/CropLens](https://github.com/FuTseYi/CropLens). GitHub redirects the former repository URL, but users should update local remotes to the canonical URL with `git remote set-url origin https://github.com/FuTseYi/CropLens.git`.

In a follow-up hygiene change, `测试图片/` was moved to `assets/samples/` without changing image contents. Tracked IDE settings and Python bytecode caches were removed from the current tree. Model weights and uploaded media remain untouched. Review third-party sample-image redistribution terms separately. **This migration does not prove that full image/video inference works.**
