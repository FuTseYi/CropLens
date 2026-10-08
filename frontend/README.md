# CropLens frontend

This directory contains the **Vue 3 / Vite** web interface for [CropLens](https://github.com/FuTseYi/CropLens), not a standalone `vue-next-admin` clone.

## Development

Run from the repository root:

```bash
cd frontend
npm ci
npm run dev
```

The development server normally runs at `http://127.0.0.1:8100` and uses Vite proxy routes for the Spring Boot and Flask services. Start the backend services separately; see the repository [quick start](../README.md) and [local configuration](../docs/CONFIGURATION.md).

```bash
npm run build
```

This command only verifies the frontend build. Model inference, database availability, external AI calls and camera support require separate testing.

## Upstream and licensing

This frontend contains code derived from the [vue-next-admin](https://gitee.com/lyt-top/vue-next-admin) admin template. Preserve applicable upstream copyright notices and check the template's original license and any third-party assets before redistributing or relicensing those portions. The project's root MIT license does not override licenses for bundled upstream material.
