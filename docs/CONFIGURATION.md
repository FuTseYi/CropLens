# CropLens — Local configuration

## Database

The Spring Boot application now reads its database login from environment variables:

- `DB_USERNAME` (defaults to `root`)
- `DB_PASSWORD` (defaults to empty — configure your local MySQL password)

The database URL currently targets `localhost:3306/cropdisease`. Import `database/schema.sql` from the repository root before starting the service. Spring Boot defaults to loopback (`127.0.0.1:9999`). `SERVER_BIND_ADDRESS` can override this for network setups, but the optional DeepSeek relay **must remain disabled** if access is exposed beyond the trusted developer machine.

```bash
export DB_USERNAME=root
export DB_PASSWORD='your-local-password'
cd backend
./mvnw spring-boot:run
```

In Windows PowerShell, use `$env:DB_PASSWORD='your-local-password'` instead of `export`.

**Security note:** An earlier commit contained a literal development database password. Replacing it in the current branch does not erase Git history; rotate any reused credentials. This repository does not claim a history rewrite.

## Python inference

```bash
cd inference
python -m pip install -r requirements.txt
python main.py
```

The Flask service also defaults to loopback (`127.0.0.1:5000`), compatible with the Spring Boot controller's local calls. This dependency list reflects inspected imports; package versions and full end-to-end compatibility have not been tested. The code refers to local relative model paths; launch commands from the inference directory.

## Frontend

```bash
cd frontend
npm ci
npm run dev
```

The Vite dev server is bound to `127.0.0.1:8100` by default with a strict port; the `VITE_HOST` environment setting controls the local bind address. The request client uses same-origin `/api` paths, while Vite proxies them to Spring Boot. Avoid publishing the developer proxy on a LAN when DeepSeek is enabled. Review application API client URLs and access controls before any public deployment. Do not treat the sample values as production configuration.

## DeepSeek text assistant (optional)

DeepSeek is used at runtime by the image advice, smart chat and greenhouse advice views. Its paid API key belongs on the Spring Boot server, **not** in Vue code or a `VITE_` variable. The local-only relay is disabled by default; see [DEEPSEEK.md](DEEPSEEK.md) for controlled opt-in setup. Do not enable it on public deployments without authentication and rate limits.

## Image confidence

The prediction threshold now respects the UI slider, and the returned confidence percentage is the YOLO model's raw confidence rather than an artificially increased value. Verify the detector using a representative sample set before claiming accuracy metrics.
