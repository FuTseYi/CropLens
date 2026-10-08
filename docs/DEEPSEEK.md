# DeepSeek integration (local development only)

CropLens uses DeepSeek at runtime for three optional features: crop-disease recommendations after image prediction, the smart chat assistant, and greenhouse environment suggestions. YOLO performs disease detection; DeepSeek generates text-based advice.

## Configuration

The three Vue pages call the **Spring Boot** endpoint `POST /ai/chat` through the existing Vite `/api` proxy. The DeepSeek credential is stored on the **server**, never in browser source or a `VITE_*` environment variable.

This endpoint is **disabled by default**. To try it on a trusted local development machine:

```bash
export DEEPSEEK_API_KEY='your-api-key'
export DEEPSEEK_PROXY_ENABLED=true
export DB_PASSWORD='your-local-database-password'
cd backend
./mvnw spring-boot:run
```

In PowerShell, set `$env:DEEPSEEK_API_KEY='your-api-key'`, `$env:DEEPSEEK_PROXY_ENABLED='true'` and `$env:DB_PASSWORD='your-local-database-password'` before starting Spring Boot. Start the Vue development server separately from `frontend` with `npm ci && npm run dev`; its `/api` proxy points to the local Spring Boot service at port 9999.

By default, the Vue dev server (`127.0.0.1:8100`), Spring Boot (`127.0.0.1:9999`) and Flask (`127.0.0.1:5000`) listen on loopback only. The DeepSeek controller rejects non-loopback callers, requests **without an Origin header**, and origins other than `http://localhost:8100` or `http://127.0.0.1:8100`. Use the application from the local browser; a bare `curl` POST without `Origin` returns `403`. Vite uses a strict port so it cannot silently switch to an unapproved origin. **These checks are not user authentication and are not a production authorization design**. Do not enable it on any public deployment; first add user authentication, rate limiting, a controlled origin policy, logging/quotas and a deployment-specific reverse proxy configuration.

## Troubleshooting

- `404` from `/api/ai/chat`: the proxy is not enabled; check `DEEPSEEK_PROXY_ENABLED=true` and restart Spring Boot.
- `503`: no server-side API key has been configured.
- `403`: the request was not made through an allowed local development route.
- `502`: the upstream provider request failed; check network access and API account status without printing the key.

The server validates the message structure, limits request size and forwards a fixed `deepseek-chat` model request. Do **not** set `VITE_HOST=0.0.0.0` or `SERVER_BIND_ADDRESS=0.0.0.0` while the paid local demo relay is enabled: reverse proxies, local software and request headers can bypass assumptions about network topology. Avoid putting private personal data in sample prompts. AI-generated agronomic recommendations are not a substitute for verified crop management advice.

## Previous instructions

Old documentation suggested editing `src/views/*/index.vue` to paste the DeepSeek API key. **Do not do that.** Frontend code is bundled into files that visitors can inspect. Any key previously pasted into a commit or distributed build must be revoked/rotated, even if later removed.
