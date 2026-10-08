
# CropLens｜智能农作物病害检测平台

**YOLO 病害检测 + 可选 DeepSeek 智能建议** · Vue 3 · Spring Boot · Flask

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Python](https://img.shields.io/badge/Python-3.8+-blue.svg)](https://www.python.org/)
[![Vue.js](https://img.shields.io/badge/Vue.js-3.x-brightgreen.svg)](https://vuejs.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.3.7-lightgrey.svg)](https://spring.io/projects/spring-boot)

[English](./README.md)

---

## 项目结构

```text
frontend/      Vue 3 + Vite 前端
backend/       Spring Boot 业务后端与本地 DeepSeek 代理
inference/     Flask + YOLO 推理服务
database/      数据库初始化脚本
assets/samples/ 示例图片
docs/          架构、部署与安全说明
```

详见 [架构文档](docs/ARCHITECTURE.md) 与 [目录迁移说明](docs/MIGRATION.md)。

## 项目简介

**CropLens** 是一个智能农作物病害检测系统，利用 **YOLO (You Only Look Once)** 模型进行实时目标检测。并提供可选的 **DeepSeek** 智能建议与对话功能（仅通过本地 Spring Boot 服务端接口访问），项目采用前后端分离架构，旨在为农业生产者和科研人员提供一个用于学习与研究的病害检测参考实现，支持从图片、视频及实时摄像头画面中识别作物病害。

## 核心功能

- **可选 DeepSeek 智能助手**: 图片诊断建议、智能聊天和温室环境建议；默认关闭，需要在可信本地环境进行服务端配置。详见 [使用与安全说明](docs/DEEPSEEK.md)。
- **多源检测**: 支持静态图片、视频文件以及实时摄像头视频流的病害检测。
- **分离式服务**: 其中 **Flask** 服务负责 AI 模型推理，**Spring Boot** 服务处理业务逻辑、数据管理和用户交互。
- **现代化前端**: 基于 **Vue 3**、**Vite** 和 **Element Plus** 构建的响应式、用户友好的 Web 界面。
- **实时通信与可视化**: 使用 **WebSocket** 在视频处理期间提供即时反馈，并集成 **ECharts** 对检测结果进行丰富的数据可视化。
- **可扩展与解耦**: 前端、业务后端和 AI 服务三者明确分离，便于独立开发、扩展和维护。

## 技术栈

- **前端**: `Vue 3`, `Vite`, `Element Plus`, `Axios`, `ECharts`, `Socket.io-client`
- **后端 (业务逻辑)**: `Java 1.8`, `Spring Boot`, `MyBatis-Plus`, `MySQL/MariaDB`, `Maven`
- **大模型建议 (可选)**: `DeepSeek Chat`，通过 Spring Boot 服务端转发，不在 Vue 中填写密钥。
- **后端 (AI 模型)**: `Python`, `Flask`, `Ultralytics (YOLO)`, `OpenCV`, `Flask-SocketIO`

## 典型应用场景

- **智慧农业**: 辅助农户快速识别作物病害，以便及时采取防治措施。
- **农业研究**: 为科研人员提供植物病理学自动数据收集和分析的工具。
- **教学示例**: 作为一个功能完善的全栈项目，供开发者学习如何将 AI 模型与 Web 应用相结合。

**本地演示限制：** 图片和视频需先通过本机 Spring Boot 上传接口保存，再交给推理服务；不接受任意外部 URL 或模型文件路径。详见 [配置说明](docs/CONFIGURATION.md)。

## 安装与快速上手

**环境依赖:**
- `Node.js` >= 16.0
- `Python` >= 3.8
- `Java` >= 1.8
- `Maven`
- `MySQL` 或 `MariaDB`

**后端启动 (Spring Boot):**
1. 进入 `backend` 目录。
2. 创建数据库，并导入根目录下的 `database/schema.sql` 文件。
3. 设置环境变量 `DB_PASSWORD`（可选设置 `DB_USERNAME`），参见 [配置说明](docs/CONFIGURATION.md)。
4. 运行应用：
   ```shell
   mvn spring-boot:run
   ```

**后端启动 (Flask AI):**
1. 进入 `inference` 目录。
2. 安装 Python 依赖：
   ```shell
   python -m pip install -r requirements.txt
   ```
   *(依赖版本尚未完整验证，详见 [配置说明](docs/CONFIGURATION.md)。)*
3. `inference/weights/` 已含不同作物的模型权重，`yolo11n.pt` 位于 Flask 目录根部；替换权重前请查看 [配置说明](docs/CONFIGURATION.md)。
4. 运行 AI 服务：
   ```shell
   python main.py
   ```

**前端启动 (Vue):**
1. 进入 `frontend` 目录。
2. 安装依赖：
   ```shell
   npm ci
   ```
3. 启动开发服务器：
   ```shell
   npm run dev
   ```
4. 在浏览器中访问提示的地址 (通常为 `http://localhost:8100`)。

## 贡献方式

欢迎参与贡献！您可以通过提交 Pull Request 或开启 Issue 来报告问题或提出新功能建议。
1. Fork 本仓库。
2. 创建您的新功能分支 (`git checkout -b feature/AmazingFeature`)。
3. 提交您的更改 (`git commit -m 'Add some AmazingFeature'`)。
4. 推送到分支 (`git push origin feature/AmazingFeature`)。
5. 创建一个 Pull Request。

## 许可证

本项目采用 **MIT 许可证**。详情请见 `LICENSE` 文件。

**文件上传说明：** 上传媒体保存在本地 `backend/files/`，新上传文件会规范化原始文件名，但尚未加入完整的内容安全扫描和访问鉴权，因此不应直接公开部署。详见 [配置说明](docs/CONFIGURATION.md)。
