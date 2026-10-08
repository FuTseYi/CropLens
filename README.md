
# CropLens

**YOLO detection + optional DeepSeek-powered advice** · Vue 3 · Spring Boot · Flask

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Python](https://img.shields.io/badge/Python-3.8+-blue.svg)](https://www.python.org/)
[![Vue.js](https://img.shields.io/badge/Vue.js-3.x-brightgreen.svg)](https://vuejs.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.3.7-lightgrey.svg)](https://spring.io/projects/spring-boot)

[简体中文](./README_zh-CN.md)

---

## Project layout

```text
frontend/      Vue 3 + Vite web interface
backend/       Spring Boot API + optional local DeepSeek relay
inference/     Flask + YOLO image/video inference
database/      SQL schema
assets/samples/ Sample images for demonstrations
docs/          Configuration and design notes
```

See [architecture](docs/ARCHITECTURE.md) and [migration notes](docs/MIGRATION.md).

## Overview

**CropLens** is an intelligent crop disease detection platform that leverages the **YOLO (You Only Look Once)** object detection models for real-time object detection. It is built with a modern, decoupled, full-stack architecture, with optional **DeepSeek** chat and agronomic text recommendations routed through a local-only Spring Boot endpoint. The system provides an end-to-end solution for identifying crop diseases from images, videos, and live camera feeds, providing a reference implementation for agricultural producers and researchers.

## Features

- **Optional AI Assistant**: DeepSeek provides text suggestions and chat in three interface views; the feature is disabled by default until locally configured. [Setup & security](docs/DEEPSEEK.md).
- **Multi-source Detection**: Supports disease detection from static images, video files, and real-time camera streams.
- **Separated Services**: Flask handles model inference; Spring Boot handles application APIs and data management.
- **Modern Frontend**: A responsive and user-friendly web interface built with **Vue 3**, **Vite**, and **Element Plus**.
- **Real-time Communication**: Utilizes **WebSocket** for instant feedback during video processing and **ECharts** for rich data visualization of detection results.
- **Scalable & Decoupled**: The clear separation of frontend, business logic, and AI services allows for independent development, scaling, and maintenance.

## Tech stack

- **Frontend**: `Vue 3`, `Vite`, `Element Plus`, `Axios`, `ECharts`, `Socket.io-client`
- **Backend (Business Logic)**: `Java 1.8`, `Spring Boot`, `MyBatis-Plus`, `MySQL/MariaDB`, `Maven`
- **LLM Advice (Optional)**: `DeepSeek Chat` via opt-in, server-side Spring Boot relay; no API key in the Vue bundle.
- **Backend (AI Model)**: `Python`, `Flask`, `Ultralytics (YOLO)`, `OpenCV`, `Flask-SocketIO`

## Use cases

- **Smart Agriculture**: Demonstrates image-based crop disease identification; decisions should be verified by qualified agronomy experts.
- **Agricultural Research**: Provides researchers with a tool for automated data collection and analysis of plant pathology.
- **Educational Tool**: Serves as a comprehensive full-stack project for developers to learn about integrating AI models with web applications.

**Local demo boundary:** Upload images/videos through the local Spring Boot service before inference; arbitrary remote URLs and model paths are not accepted. See [configuration](docs/CONFIGURATION.md).

## Quick start

**Prerequisites:**
- `Node.js` >= 16.0
- `Python` >= 3.8
- `Java` >= 1.8
- `Maven`
- `MySQL` or `MariaDB`

**Backend Setup (Spring Boot):**
1. Navigate to the `backend` directory.
2. Create a database and import the `database/schema.sql` file.
3. Set `DB_PASSWORD` (and optionally `DB_USERNAME`) in your environment; see [configuration guidance](docs/CONFIGURATION.md).
4. Run the application:
   ```shell
   mvn spring-boot:run
   ```

**Backend Setup (Flask AI):**
1. Navigate to the `inference` directory.
2. Install Python dependencies:
   ```shell
   python -m pip install -r requirements.txt
   ```
   *(Dependency versions have not yet been validated; see [configuration guidance](docs/CONFIGURATION.md).)*
3. Existing crop-specific weights are in `weights/` and `yolo11n.pt` is at the inference directory root; refer to [configuration](docs/CONFIGURATION.md) before replacing model files.
4. Run the AI service:
   ```shell
   python main.py
   ```

**Frontend Setup (Vue):**
1. Navigate to the `frontend` directory.
2. Install dependencies:
   ```shell
   npm ci
   ```
3. Start the development server:
   ```shell
   npm run dev
   ```
4. Access the application at the address provided (typically `http://localhost:8100`).

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request or open an Issue to report bugs or suggest new features.
1. Fork the repository.
2. Create your feature branch (`git checkout -b feature/AmazingFeature`).
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

## License

This project is licensed under the **MIT License**. See the `LICENSE` file for details.
