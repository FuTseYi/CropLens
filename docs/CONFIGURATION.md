# CropLens — Local configuration

## Database

The Spring Boot application now reads its database login from environment variables:

- `DB_USERNAME` (defaults to `root`)
- `DB_PASSWORD` (defaults to empty — configure your local MySQL password)

The database URL currently targets `localhost:3306/cropdisease`. Import `cropdisease.sql` before starting the service.

```bash
export DB_USERNAME=root
export DB_PASSWORD='your-local-password'
cd YOLO_AI_CropDisease_Detection_SpringBoot
./mvnw spring-boot:run
```

In Windows PowerShell, use `$env:DB_PASSWORD='your-local-password'` instead of `export`.

**Security note:** An earlier commit contained a literal development database password. Replacing it in the current branch does not erase Git history; rotate any reused credentials. This repository does not claim a history rewrite.

## Python inference

```bash
cd YOLO_AI_CropDisease_Detection_Flask
python -m pip install -r requirements.txt
python main.py
```

This dependency list reflects inspected imports; package versions and full end-to-end compatibility have not been tested. The code refers to local relative model paths; launch commands from the inference directory.

## Frontend

```bash
cd YOLO_AI_CropDisease_Detection_Vue
npm ci
npm run dev
```

Review the Vite `.env.*` files and application API client URLs for the actual ports and hosts before deployment. Do not treat the sample values as production configuration.
