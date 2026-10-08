
# CropLens

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Python](https://img.shields.io/badge/Python-3.8+-blue.svg)](https://www.python.org/)
[![Vue.js](https://img.shields.io/badge/Vue.js-3.x-brightgreen.svg)](https://vuejs.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.3.7-lightgrey.svg)](https://spring.io/projects/spring-boot)

[中文](./README_zh-CN.md)

---

![Star History Chart](https://api.star-history.com/svg?repos=FuTseYi/YOLO_DeepSeek_Powered_CropDisease_Detection&type=Date)

### 1. Project Overview

This project is an intelligent crop disease detection system that leverages the powerful **YOLO (You Only Look Once)** algorithm for real-time object detection. It is built with a modern, decoupled, full-stack architecture, assisted by the **DeepSeek** AI model for code generation and project insights. The system provides an end-to-end solution for identifying crop diseases from images, videos, and live camera feeds, aiming to offer an efficient and accurate tool for agricultural producers and researchers.

### 2. Core Features

- **Multi-source Detection**: Supports disease detection from static images, video files, and real-time camera streams.
- **High-Performance Backend**: A microservices architecture featuring a **Flask** server for handling AI model inferences and a **Spring Boot** server for business logic, data management, and user interactions.
- **Modern Frontend**: A responsive and user-friendly web interface built with **Vue 3**, **Vite**, and **Element Plus**.
- **Real-time Communication**: Utilizes **WebSocket** for instant feedback during video processing and **ECharts** for rich data visualization of detection results.
- **Scalable & Decoupled**: The clear separation of frontend, business logic, and AI services allows for independent development, scaling, and maintenance.

### 3. Technology Stack

- **Frontend**: `Vue 3`, `Vite`, `Element Plus`, `Axios`, `ECharts`, `Socket.io-client`
- **Backend (Business Logic)**: `Java 1.8`, `Spring Boot`, `MyBatis-Plus`, `MySQL/MariaDB`, `Maven`
- **Backend (AI Model)**: `Python`, `Flask`, `Ultralytics (YOLO)`, `OpenCV`, `Flask-SocketIO`

### 4. Application Scenarios

- **Smart Agriculture**: Assists farmers in quickly identifying crop diseases for timely intervention.
- **Agricultural Research**: Provides researchers with a tool for automated data collection and analysis of plant pathology.
- **Educational Tool**: Serves as a comprehensive full-stack project for developers to learn about integrating AI models with web applications.

### 5. Installation and Quick Start

**Prerequisites:**
- `Node.js` >= 16.0
- `Python` >= 3.8
- `Java` >= 1.8
- `Maven`
- `MySQL` or `MariaDB`

**Backend Setup (Spring Boot):**
1. Navigate to the `backend` directory.
2. Create a database and import the `database/schema.sql` file.
3. Modify the database connection settings in `src/main/resources/application.properties`.
4. Run the application:
   ```shell
   mvn spring-boot:run
   ```

**Backend Setup (Flask AI):**
1. Navigate to the `inference` directory.
2. Install Python dependencies:
   ```shell
   pip install -r requirements.txt
   ```
   *(Note: A `requirements.txt` file should be created with libraries like `ultralytics`, `flask`, `opencv-python`, `requests`, `flask-socketio`)*
3. Download the pre-trained YOLO model weights (e.g., `yolo11n.pt`) and place them in the `weights` folder.
4. Run the AI service:
   ```shell
   python main.py
   ```

**Frontend Setup (Vue):**
1. Navigate to the `frontend` directory.
2. Install dependencies:
   ```shell
   npm install
   ```
3. Start the development server:
   ```shell
   npm run dev
   ```
4. Access the application at the address provided (e.g., `http://localhost:3000`).

### 6. Contribution

Contributions are welcome! Please feel free to submit a Pull Request or open an Issue to report bugs or suggest new features.
1. Fork the repository.
2. Create your feature branch (`git checkout -b feature/AmazingFeature`).
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

### 7. License

This project is licensed under the **MIT License**. See the `LICENSE` file for details.
