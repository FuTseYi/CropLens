
# CropLens｜智能农作物病害检测平台

**基于 YOLO 的作物病害检测系统** · Vue 3 · Spring Boot · Flask

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Python](https://img.shields.io/badge/Python-3.8+-blue.svg)](https://www.python.org/)
[![Vue.js](https://img.shields.io/badge/Vue.js-3.x-brightgreen.svg)](https://vuejs.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.3.7-lightgrey.svg)](https://spring.io/projects/spring-boot)

[English](./README.md)

---

### 1. 项目简介

**CropLens** 是一个智能农作物病害检测系统，利用强大的 **YOLO (You Only Look Once)** 算法进行实时目标检测。在 **DeepSeek** AI 模型的辅助下，项目采用现代化的、前后端分离的全栈架构构建，旨在为农业生产者和科研人员提供一个高效、精准的病害识别工具，支持从图片、视频及实时摄像头画面中识别作物病害。

### 2. 核心功能

- **多源检测**: 支持静态图片、视频文件以及实时摄像头视频流的病害检测。
- **高性能后端**: 采用微服务架构，其中 **Flask** 服务负责 AI 模型推理，**Spring Boot** 服务处理业务逻辑、数据管理和用户交互。
- **现代化前端**: 基于 **Vue 3**、**Vite** 和 **Element Plus** 构建的响应式、用户友好的 Web 界面。
- **实时通信与可视化**: 使用 **WebSocket** 在视频处理期间提供即时反馈，并集成 **ECharts** 对检测结果进行丰富的数据可视化。
- **可扩展与解耦**: 前端、业务后端和 AI 服务三者明确分离，便于独立开发、扩展和维护。

### 3. 技术栈

- **前端**: `Vue 3`, `Vite`, `Element Plus`, `Axios`, `ECharts`, `Socket.io-client`
- **后端 (业务逻辑)**: `Java 1.8`, `Spring Boot`, `MyBatis-Plus`, `MySQL/MariaDB`, `Maven`
- **后端 (AI 模型)**: `Python`, `Flask`, `Ultralytics (YOLO)`, `OpenCV`, `Flask-SocketIO`

### 4. 典型应用场景

- **智慧农业**: 辅助农户快速识别作物病害，以便及时采取防治措施。
- **农业研究**: 为科研人员提供植物病理学自动数据收集和分析的工具。
- **教学示例**: 作为一个功能完善的全栈项目，供开发者学习如何将 AI 模型与 Web 应用相结合。

### 5. 安装与快速上手

**环境依赖:**
- `Node.js` >= 16.0
- `Python` >= 3.8
- `Java` >= 1.8
- `Maven`
- `MySQL` 或 `MariaDB`

**后端启动 (Spring Boot):**
1. 进入 `YOLO_AI_CropDisease_Detection_SpringBoot` 目录。
2. 创建数据库，并导入根目录下的 `cropdisease.sql` 文件。
3. 设置环境变量 `DB_PASSWORD`（可选设置 `DB_USERNAME`），参见 [配置说明](docs/CONFIGURATION.md)。
4. 运行应用：
   ```shell
   mvn spring-boot:run
   ```

**后端启动 (Flask AI):**
1. 进入 `YOLO_AI_CropDisease_Detection_Flask` 目录。
2. 安装 Python 依赖：
   ```shell
   python -m pip install -r requirements.txt
   ```
   *(依赖版本尚未完整验证，详见 [配置说明](docs/CONFIGURATION.md)。)*
3. 下载预训练的 YOLO 模型权重文件 (如 `yolo11n.pt`) 并放入 `weights` 文件夹。
4. 运行 AI 服务：
   ```shell
   python main.py
   ```

**前端启动 (Vue):**
1. 进入 `YOLO_AI_CropDisease_Detection_Vue` 目录。
2. 安装依赖：
   ```shell
   npm install
   ```
3. 启动开发服务器：
   ```shell
   npm run dev
   ```
4. 在浏览器中访问提示的地址 (例如 `http://localhost:3000`)。

### 6. 贡献方式

欢迎参与贡献！您可以通过提交 Pull Request 或开启 Issue 来报告问题或提出新功能建议。
1. Fork 本仓库。
2. 创建您的新功能分支 (`git checkout -b feature/AmazingFeature`)。
3. 提交您的更改 (`git commit -m 'Add some AmazingFeature'`)。
4. 推送到分支 (`git push origin feature/AmazingFeature`)。
5. 创建一个 Pull Request。

### 7. 许可证

本项目采用 **MIT 许可证**。详情请见 `LICENSE` 文件。
