# CropLens — Repository architecture

The repository contains three separately started services. This document describes the **current checked-in layout**, not a proposed migration.

| Directory | Purpose |
| --- | --- |
| `YOLO_AI_CropDisease_Detection_Vue/` | Vue 3 / Vite user interface |
| `YOLO_AI_CropDisease_Detection_SpringBoot/` | Java Spring Boot business API, persistence and uploaded assets |
| `YOLO_AI_CropDisease_Detection_Flask/` | Python Flask / YOLO inference API, training entry point and model assets |
| `cropdisease.sql` | Database import script |
| `测试图片/` | Existing detection samples |

## Runtime overview

```text
Browser (Vue)
   ├── Spring Boot service → database / uploaded files
   │    └── optional /ai/chat → DeepSeek text recommendations (local-only)
   └── Flask inference service → YOLO model weights
```

The service endpoints and credentials must be checked in their respective configuration files before deployment. Do not commit production API keys, database credentials or user datasets.

## Planned cleanup (not yet performed)

- Audit configuration files for credentials and sample values before publishing more setup examples.
- Verify inter-service URLs and file paths before shortening module directories.
- Keep existing model weights accessible until inference paths have been tested.
- Remove tracked generated output only after confirming that no demo or runtime relies on it.

**DeepSeek runtime role:** Three Vue pages request text suggestions / chat through Spring Boot's optional local-only relay. YOLO remains the disease-detection inference engine. See [DEEPSEEK.md](DEEPSEEK.md).
