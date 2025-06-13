# Object Detection Platform 🎯

![Object Detection Platform](https://github.com/user-attachments/assets/a506f359-fab6-4680-a1a5-d496b99427b1)

![Java](https://img.shields.io/badge/Java-11+-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7-green?style=for-the-badge&logo=spring)
![Android](https://img.shields.io/badge/Android-SDK-brightgreen?style=for-the-badge&logo=android)
![Node.js](https://img.shields.io/badge/Node.js-14+-yellow?style=for-the-badge&logo=node.js)

**A comprehensive real-time object detection platform with AI-powered image analysis**

## 📚 Table of Contents

- [Overview](#-overview)
- [Architecture](#-architecture)
- [Demo](#-demo)
- [Dashboard Features](#-dashboard-features)
- [Android Example App](#-android-example-app)
- [Use Cases](#-use-cases)
- [Performance Optimization](#-performance-optimization)
- [Contributing](#-contributing)
- [License](#-license)
- [Links & Resources](#-links--resources)
- [Acknowledgments](#-acknowledgments)

---

## 🌟 Overview

The Object Detection Platform is a full-stack solution that brings AI-powered object detection to your fingertips. Built with modern technologies and designed for scalability, it offers seamless integration across web and mobile platforms.

### ✨ Key Features

- 🎯 **Real-time Object Detection** - Live camera feed analysis with instant overlay visualization
- 📱 **Cross-Platform SDK** - Easy Android integration with minimal setup
- 🌐 **Web Dashboard** - Comprehensive monitoring and analytics portal  
- ⚡ **High Performance** - Optimized for speed with advanced coordinate transformation
- 🤖 **AI-Powered** - Facebook's DETR model via Hugging Face integration
- ☁️ **Cloud Ready** - Scalable architecture with Cloudinary storage
- 📊 **Analytics** - Real-time metrics and detection history tracking

---

## 🏗️ Architecture

```mermaid
graph TB
    subgraph "Client Layer"
        A[Android App] 
        B[Web Dashboard]
    end
    
    subgraph "API Layer"
        C[Spring Boot API]
        D[REST Endpoints]
    end
    
    subgraph "AI Layer"
        E[Hugging Face DETR]
        F[Object Detection Model]
    end
    
    subgraph "Storage Layer"
        G[Cloudinary]
        H[In-Memory Analytics]
    end
    
    A --> C
    B --> C
    C --> D
    D --> E
    E --> F
    C --> G
    C --> H
    
    style A fill:#4CAF50
    style B fill:#2196F3
    style C fill:#FF9800
    style E fill:#9C27B0
    style G fill:#F44336
```

### 🔧 Technology Stack

| Layer | Technology | Purpose |
|-------|------------|---------|
| **Frontend** | Bootstrap 5, Chart.js, Vanilla JS | Responsive web dashboard |
| **Backend** | Spring Boot 2.7, Java 11 | RESTful API service |
| **Mobile** | Android SDK, CameraX, Retrofit | Native mobile integration |
| **AI/ML** | Hugging Face DETR (Facebook) | Object detection processing |
| **Storage** | Cloudinary, H2 Database | Image hosting & analytics |
| **Monitoring** | Node.js, Express | Dashboard backend |

---

## 🎬 Demo

### 📱 Android App in Action

| Main Menu | Live Detection | Results View |
|-----------|----------------|--------------|
| ![Main Menu](https://github.com/user-attachments/assets/4e055fe2-2b69-4fc2-bdfc-2bf7a5608d51) | ![Live Detection](https://github.com/user-attachments/assets/affe2fa5-9c9b-49b4-81f0-40a3b0011daa) | ![Results](https://github.com/user-attachments/assets/8d335d49-758c-40a3-bf47-b74fad1ce91a) |

### 🌐 Web Dashboard

<p align="center">
  <img src="https://github.com/user-attachments/assets/1faa615b-a4cd-49c1-8c7d-c68a11ead664" width="45%"/>
  <img src="https://github.com/user-attachments/assets/502e1fec-a2cd-4161-8c81-bc74467741c0" width="45%"/><br>
  <img src="https://github.com/user-attachments/assets/afac89a2-8d3b-4e6f-afec-5446ad61fdc3" width="45%"/>
  <img src="https://github.com/user-attachments/assets/a46e2da4-9d02-4399-8d58-dc1690a21e7d" width="45%"/><br>
  <img src="https://github.com/user-attachments/assets/5c8f39e2-5eeb-493c-ba8b-2da2fbc318b0" width="45%"/>
  <img src="https://github.com/user-attachments/assets/34a0f47c-e464-4201-a7eb-062689164e53" width="45%"/>
</p>

🎥 **Demo Video** 
[Click here to watch the entire Web Dashboard](https://github.com/user-attachments/assets/5502927b-8e82-44cc-abae-c89344c2165b)


*Real-time analytics dashboard with live metrics and detection management*

### 🎯 Detection Examples

<table>
  <tr>
    <th>Original Image</th>
    <th>Detection Result</th>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/5d48fd8b-9752-4b00-bba2-3a5c7c2b8b16" width="300"/></td>
    <td><img src="https://github.com/user-attachments/assets/3e403fa1-11e6-4673-b8b4-fdf28ec7b522" width="300"/></td>
  </tr>
</table>

*Example: spoon detection with 99.7% confidence*  
*Example: spoon detection with 99.5% confidence*  
*Example: fork detection with 99.2% confidence*  
*Example: knife detection with 87.5% confidence*  
*Example: dining table detection with 61.3% confidence*

---

## 📊 Dashboard Features

### Real-time Metrics
- 📈 **Live Analytics** - API calls, response times, error rates
- 📊 **Performance Charts** - Interactive visualizations with Chart.js
- 🔍 **Detection History** - Browse and filter past detections
- ⚙️ **System Health** - Monitor service status and uptime

### Detection Management
- 🖼️ **Image Upload** - Direct web-based detection
- 🔗 **URL Processing** - Analyze images from URLs
- 📱 **Device Tracking** - Monitor detection sources
- 💾 **Results Storage** - Persistent detection history

### Configuration
- 🎛️ **Detection Parameters** - Confidence thresholds and limits
- 🎨 **Visualization Settings** - Overlay customization
- 🔧 **API Configuration** - Endpoint and timeout settings
- 📋 **Model Management** - Switch between AI models

---

## 📱 Android Example App

The included example app demonstrates all SDK capabilities:

### Features Showcase

- **📷 Camera Detection** - Real-time object detection with camera preview
- **🖼️ Gallery Integration** - Select and analyze images from gallery
- **🔗 URL Processing** - Analyze images from web URLs
- **⚙️ Settings Management** - Configure API endpoints and preferences
- **📊 Results Display** - Detailed detection results with confidence scores

---

## 🎯 Use Cases

### 🏢 Business Applications

- **🛡️ Security Systems** - Automated surveillance and monitoring
- **🏪 Retail Analytics** - Customer behavior and product tracking  
- **🚗 Traffic Management** - Vehicle and pedestrian counting
- **🏥 Healthcare** - Medical image analysis assistance
- **📱 Mobile Apps** - Enhanced camera functionality

### 👨‍💻 Developer Benefits

- **⚡ Rapid Prototyping** - Quick integration with minimal code
- **🔧 Flexible Architecture** - Modular components for custom solutions
- **📊 Built-in Analytics** - Comprehensive monitoring out of the box
- **🌐 Cross-Platform** - Consistent API across web and mobile
- **📚 Comprehensive Docs** - Detailed guides and examples

---

### Performance Optimization

#### ⚡ API Performance

- **Connection Pooling** - Reuse HTTP connections
- **Response Caching** - Cache repeated requests
- **Image Optimization** - Compress images before processing

#### 📱 Mobile Performance

- **Frame Skipping** - Process every N-th frame for live detection
- **Background Processing** - Use separate threads for heavy operations
- **Memory Management** - Properly dispose of camera resources

---

## 🤝 Contributing

We welcome contributions! Here's how to get started:

### Development Workflow

1. **🍴 Fork** the repository
2. **🌿 Create** a feature branch: `git checkout -b feature/amazing-feature`
3. **✨ Make** your changes with tests
4. **✅ Commit** changes: `git commit -m 'Add amazing feature'`
5. **🚀 Push** to branch: `git push origin feature/amazing-feature`
6. **📬 Open** a Pull Request

### Code Style

- **Java**: Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- **JavaScript**: Use [Airbnb JavaScript Style Guide](https://github.com/airbnb/javascript)
- **Android**: Follow [Android Code Style](https://source.android.com/setup/contribute/code-style)

### Areas for Contribution

- 🐛 **Bug Fixes** - Help us squash bugs
- ✨ **New Features** - Add exciting capabilities
- 📚 **Documentation** - Improve guides and examples
- 🧪 **Testing** - Increase test coverage
- 🎨 **UI/UX** - Enhance user experience
- 🔧 **Performance** - Optimize algorithms and architecture

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](https://github.com/EliorMauda/Advanced_Seminar_in_Mobile_Development_2025/blob/master/LICENSE) file for details.

---

## 🔗 Links & Resources

- 📖 **[Complete Documentation](https://EliorMauda.github.io/Advanced_Seminar_in_Mobile_Development_2025/)**
- 🐛 **[Report Issues](https://github.com/EliorMauda/Advanced_Seminar_in_Mobile_Development_2025/issues)**
- 💬 **[Discussions](https://github.com/EliorMauda/Advanced_Seminar_in_Mobile_Development_2025/discussions)**
- 🤖 **[Hugging Face DETR Model](https://huggingface.co/facebook/detr-resnet-50)**

---

## 🙏 Acknowledgments

- **Facebook AI Research** - For the DETR object detection model
- **Hugging Face** - For making AI models accessible
- **Cloudinary** - For reliable image hosting
- **Android Team** - For CameraX and modern Android APIs
- **Spring Boot Community** - For the excellent framework

---

## 📈 Project Stats

![GitHub repo size](https://img.shields.io/github/repo-size/EliorMauda/Advanced_Seminar_in_Mobile_Development_2025)
![GitHub last commit](https://img.shields.io/github/last-commit/EliorMauda/Advanced_Seminar_in_Mobile_Development_2025)
![GitHub issues](https://img.shields.io/github/issues/EliorMauda/Advanced_Seminar_in_Mobile_Development_2025)
![GitHub pull requests](https://img.shields.io/github/issues-pr/EliorMauda/Advanced_Seminar_in_Mobile_Development_2025)

---

**Built by [Elior Mauda](https://github.com/EliorMauda)**

⭐ **Star this repository if you found it helpful!** ⭐
