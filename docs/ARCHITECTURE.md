# CropLens — Repository architecture

The repository contains three separately started services. This document describes the **current checked-in layout**, not a proposed migration.

| Directory | Purpose |
| --- | --- |
| `frontend/` | Vue 3 / Vite user interface |
| `backend/` | Java Spring Boot business API, persistence and uploaded assets |
| `inference/` | Python Flask / YOLO inference API, training entry point and model assets |
| `database/schema.sql` | Database import script |
| `assets/samples/` | Existing demonstration images (relocated without modifying image content) |

## Runtime overview

```text
Browser (Vue)
   ├── Spring Boot service → database / uploaded files
   │    └── optional /ai/chat → DeepSeek text recommendations (local-only)
   └── Flask inference service → YOLO model weights
```

The service endpoints and credentials must be checked in their respective configuration files before deployment. Do not commit production API keys, database credentials or user datasets.

## Follow-up work

- Audit configuration files for credentials and sample values before publishing more setup examples.
- Keep service-local relative paths valid after the top-level module rename; the files inside each service have not been reorganized.
- Keep existing model weights accessible until inference paths have been tested.
- Keep training assets and runtime-generated uploads distinct from static demonstration samples.
- Confirm dataset redistribution terms before republishing external images.

**DeepSeek runtime role:** Three Vue pages request text suggestions / chat through Spring Boot's optional local-only relay. YOLO remains the disease-detection inference engine. See [DEEPSEEK.md](DEEPSEEK.md).

**Layout migration:** The service directories and SQL schema have moved. See [MIGRATION.md](MIGRATION.md) for the exact rename map. Existing local clones should re-clone or pull and update their working directories.

**Repository hygiene:** Top-level `.idea/`, inference IDE settings and cached Python bytecode have been removed from the current tree. The originals remain in Git history. Existing image examples were moved without recompression to `assets/samples/`.
