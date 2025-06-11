# Object Detection Platform Documentation

![Object Detection Platform](https://img.shields.io/badge/Object%20Detection-Platform-blue?style=for-the-badge&logo=camera)
![API](https://img.shields.io/badge/API-Spring%20Boot-green?style=flat-square&logo=spring)
![Android SDK](https://img.shields.io/badge/Android-SDK-orange?style=flat-square&logo=android)
![Dashboard](https://img.shields.io/badge/Dashboard-Node.js-yellow?style=flat-square&logo=node.js)

## Table of Contents

1. [Overview](#overview)
2. [Architecture](#architecture)
3. [Quick Start](#quick-start)
4. [API Service](#api-service)
5. [Android SDK](#android-sdk)
6. [Android Example App](#android-example-app)
7. [Dashboard Portal](#dashboard-portal)
8. [Deployment](#deployment)
9. [Troubleshooting](#troubleshooting)
10. [Contributing](#contributing)

## Overview

The Object Detection Platform is a comprehensive solution for real-time object detection in images and camera feeds. It consists of four main components:

- **Spring Boot API Service**: Backend service with Hugging Face DETR model integration
- **Android SDK**: Easy-to-integrate library for Android applications
- **Example Android App**: Demonstration of SDK capabilities
- **Web Dashboard**: Real-time monitoring and management portal

### Key Features

- 🎯 **Real-time Detection**: Live camera object detection with overlay visualization
- 📱 **Multi-platform**: File upload, URI processing, and URL-based detection
- 📊 **Analytics Dashboard**: Comprehensive monitoring and statistics
- 🔧 **Easy Integration**: Drop-in Android SDK with minimal setup
- ⚡ **High Performance**: Optimized frame processing and coordinate transformation
- 🌐 **Cloud Ready**: Scalable architecture with Cloudinary integration

## Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Android App   │───▶│  Spring Boot    │───▶│  Hugging Face   │
│                 │    │     API         │    │     DETR        │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         │              ┌─────────────────┐              │
         └──────────────▶│   Dashboard     │              │
                        │    Portal       │              │
                        └─────────────────┘              │
                                 │                       │
                        ┌─────────────────┐              │
                        │   Cloudinary    │◀─────────────┘
                        │   Storage       │
                        └─────────────────┘
```

### Technology Stack

| Component | Technology | Purpose |
|-----------|------------|---------|
| **Backend API** | Spring Boot 2.7, Java 11 | REST API with detection endpoints |
| **AI Model** | Hugging Face DETR (Facebook) | Object detection processing |
| **Android SDK** | Java, CameraX, Retrofit | Mobile integration library |
| **Dashboard** | Node.js, Express, Chart.js | Monitoring and management |
| **Storage** | Cloudinary | Image hosting and management |
| **Database** | In-memory (H2) | Analytics and session storage |

## Quick Start

### Prerequisites

- Java 11 or higher
- Android Studio Arctic Fox or later
- Node.js 14 or higher
- Git

### 1. Start the API Service

```bash
cd backend-api
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`

### 2. Start the Dashboard

```bash
cd dashboard-portal
npm install
npm start
```

The dashboard will be available at `http://localhost:3000`

### 3. Test the Integration

1. Open the dashboard at `http://localhost:3000`
2. Upload an image or provide an image URL
3. View detection results in real-time

## API Service

### Overview

The Spring Boot API service provides RESTful endpoints for object detection using the Hugging Face DETR model. It supports multiple input methods and provides comprehensive analytics.

### Configuration

#### Environment Variables

Create an `application.properties` file or set environment variables:

```properties
# Cloudinary Configuration
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret

# Hugging Face API
HUGGINGFACE_API_TOKEN=your_token

# Server Configuration
SERVER_PORT=8080
```

#### Hugging Face Setup

1. Create an account at [Hugging Face](https://huggingface.co/)
2. Generate an API token from your profile settings
3. Set the token in your environment variables

#### Cloudinary Setup

1. Create an account at [Cloudinary](https://cloudinary.com/)
2. Get your cloud name, API key, and API secret from the dashboard
3. Configure the credentials in your environment

### API Endpoints

#### Detection Endpoints

##### Upload Image File
```http
POST /api/detect
Content-Type: multipart/form-data

Parameters:
- image: File (required) - Image file (JPG, PNG, BMP)
```

**Example using cURL:**
```bash
curl -X POST \
  http://localhost:8080/api/detect \
  -H 'Content-Type: multipart/form-data' \
  -F 'image=@path/to/your/image.jpg'
```

##### Detect from URL
```http
POST /api/detect/url
Content-Type: application/json

{
  "url": "https://example.com/image.jpg"
}
```

**Example using cURL:**
```bash
curl -X POST \
  http://localhost:8080/api/detect/url \
  -H 'Content-Type: application/json' \
  -d '{"url": "https://example.com/image.jpg"}'
```

#### Dashboard Endpoints

```http
GET /api/dashboard/metrics           # Real-time metrics
GET /api/dashboard/chart-data        # Chart data for analytics
GET /api/dashboard/system-status     # System health status
GET /api/dashboard/recent-detections # Recent detection history
```

### Response Format

All detection endpoints return a standardized `DetectionResult`:

```json
{
  "imageUrl": "https://res.cloudinary.com/...",
  "detectedObjects": [
    {
      "label": "person",
      "confidence": 0.9847,
      "box": {
        "xMin": 0.123,
        "yMin": 0.456,
        "xMax": 0.789,
        "yMax": 0.834
      }
    }
  ],
  "processingTimeMs": 1250,
  "error": null
}
```

### Error Handling

The API provides comprehensive error handling with appropriate HTTP status codes:

- `400 Bad Request`: Invalid input or malformed requests
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Processing failures

```json
{
  "error": "Invalid image format. Supported formats: JPG, PNG, BMP",
  "processingTimeMs": 45
}
```

### Running the API

#### Development Mode

```bash
cd backend-api
./mvnw spring-boot:run
```

#### Production Mode

```bash
./mvnw clean package
java -jar target/backend-api-0.1.0.jar
```

#### Docker Deployment

```dockerfile
FROM openjdk:11-jre-slim
COPY target/backend-api-0.1.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## Android SDK

### Overview

The Android Object Detection SDK provides easy integration of object detection capabilities into Android applications. It supports both static image processing and real-time camera detection with advanced overlay visualization.

### Installation

#### Option 1: JitPack (Recommended)

Add to your app-level `build.gradle`:

```gradle
dependencies {
    implementation 'com.github.EliorMauda:android-object-detection-sdk:v0.1.5'
}
```

Add to your project-level `build.gradle`:

```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

#### Option 2: Local AAR

1. Download the latest `android-sdk-release.aar`
2. Copy to your app's `libs` folder
3. Add to `build.gradle`:

```gradle
dependencies {
    implementation files('libs/android-sdk-release.aar')
}
```

### Setup

#### 1. Initialize the SDK

```java
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        
        // Initialize with the API URL
        ImageDetector.init("https://object-detection-api-production.up.railway.app");
    }
}
```

#### 2. Add Permissions

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

### Basic Usage

#### Static Image Detection

```java
// From file
File imageFile = new File(imagePath);
ImageDetector.detectFromFile(imageFile, new ImageDetectionListener() {
    @Override
    public void onResult(DetectionResult result) {
        if (result.isSuccess()) {
            List<DetectedObject> objects = result.getDetectedObjects();
            // Process detected objects
            updateUI(objects);
        }
    }

    @Override
    public void onError(Exception e) {
        Log.e(TAG, "Detection failed", e);
    }
});

// From URI (gallery, camera)
Uri imageUri = data.getData(); // From image picker
ImageDetector.detectFromUri(this, imageUri, detectionListener);

// From URL
String imageUrl = "https://example.com/image.jpg";
ImageDetector.detectFromUrl(imageUrl, detectionListener);
```

#### Builder Pattern

```java
DetectorBuilder.with(this)
    .setListener(new ImageDetectionListener() {
        @Override
        public void onResult(DetectionResult result) {
            // Handle result
        }
        
        @Override
        public void onError(Exception e) {
            // Handle error
        }
    })
    .detectFromFile(imageFile);
```

### Live Camera Detection

#### 1. Setup Layout

```xml
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.camera.view.PreviewView
        android:id="@+id/previewView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

    <com.objectdetection.sdk.view.DetectionOverlayView
        android:id="@+id/overlayView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</FrameLayout>
```

#### 2. Start Live Detection

```java
public class LiveDetectionActivity extends AppCompatActivity {
    private PreviewView previewView;
    private DetectionOverlayView overlayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_detection);
        
        previewView = findViewById(R.id.previewView);
        overlayView = findViewById(R.id.overlayView);
        
        startLiveDetection();
    }

    private void startLiveDetection() {
        ImageDetector.startLiveDetection(this, previewView, new LiveDetectionListener() {
            @Override
            public void onDetectionResult(DetectionResult result, long frameTimestamp) {
                // Update overlay with proper coordinate transformation
                updateOverlay(result);
            }

            @Override
            public void onError(Exception e, long frameTimestamp) {
                Log.e(TAG, "Live detection error", e);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageDetector.stopLiveDetection();
    }
}
```

### Advanced Overlay Configuration

The SDK provides advanced coordinate transformation for pixel-perfect overlay alignment:

```java
private void updateOverlay(DetectionResult result) {
    // Get camera frame dimensions
    int frameWidth = 1920;  // Actual camera resolution
    int frameHeight = 1080;
    
    // Get preview view dimensions
    int previewWidth = previewView.getWidth();
    int previewHeight = previewView.getHeight();
    
    // Calculate display transformation
    float previewAspectRatio = (float) previewWidth / previewHeight;
    float frameAspectRatio = (float) frameWidth / frameHeight;
    
    int displayWidth, displayHeight, offsetX = 0, offsetY = 0;
    
    if (frameAspectRatio > previewAspectRatio) {
        displayWidth = previewWidth;
        displayHeight = (int) (previewWidth / frameAspectRatio);
        offsetY = (previewHeight - displayHeight) / 2;
    } else {
        displayHeight = previewHeight;
        displayWidth = (int) (previewHeight * frameAspectRatio);
        offsetX = (previewWidth - displayWidth) / 2;
    }
    
    // Apply transformation to overlay
    overlayView.setDetectionResult(result, frameWidth, frameHeight,
                                 displayWidth, displayHeight, offsetX, offsetY);
}
```

### Custom Overlay Styling

```java
public class CustomOverlayView extends DetectionOverlayView {
    
    @Override
    protected void onDraw(Canvas canvas) {
        // Custom drawing logic
        super.onDraw(canvas);
        
        // Add custom annotations
        drawCustomLabels(canvas);
    }
    
    private void drawCustomLabels(Canvas canvas) {
        // Your custom overlay implementation
    }
}
```

### Error Handling Best Practices

```java
ImageDetectionListener robustListener = new ImageDetectionListener() {
    @Override
    public void onResult(DetectionResult result) {
        if (result.isSuccess()) {
            processResults(result.getDetectedObjects());
        } else {
            handleDetectionFailure(result.getError());
        }
    }

    @Override
    public void onError(Exception e) {
        if (e instanceof FileNotFoundException) {
            showError("Image file not found");
        } else if (e.getMessage().contains("Network")) {
            showError("Check internet connection");
        } else {
            showError("Detection failed: " + e.getMessage());
        }
    }
};
```

## Android Example App

### Overview

The example application demonstrates all SDK capabilities with a complete, production-ready implementation including camera detection, gallery integration, and URL processing.

### Features

- **Multiple Input Sources**: Camera capture, gallery selection, URL input
- **Live Camera Detection**: Real-time object detection with overlay
- **Results Visualization**: Detailed detection results with confidence scores
- **Settings Management**: API URL configuration and preferences
- **Error Handling**: Comprehensive error management and user feedback

### Key Components

#### MainActivity

The main entry point provides three detection options:

```java
public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeSDK();
        setupButtons();
    }
    
    private void initializeSDK() {
        String apiUrl = getPreferences().getString("api_url", DEFAULT_API_URL);
        ImageDetector.init(apiUrl);
    }
}
```

#### DetectionResultActivity

Displays detailed detection results with proper overlay positioning:

```java
public class DetectionResultActivity extends AppCompatActivity {
    
    private void displayDetectionResult(DetectionResult result) {
        // Calculate proper image scaling for overlay
        Drawable drawable = imageView.getDrawable();
        int originalWidth = drawable.getIntrinsicWidth();
        int originalHeight = drawable.getIntrinsicHeight();
        
        // Apply coordinate transformation
        overlayView.setDetectionResult(result, originalWidth, originalHeight,
                                     displayWidth, displayHeight, offsetX, offsetY);
    }
}
```

#### LiveDetectionActivity

Implements real-time camera detection:

```java
public class LiveDetectionActivity extends AppCompatActivity {
    
    private void startDetection() {
        ImageDetector.startLiveDetection(this, previewView, new LiveDetectionListener() {
            @Override
            public void onDetectionResult(DetectionResult result, long frameTimestamp) {
                runOnUiThread(() -> updateOverlay(result));
            }
            
            @Override
            public void onError(Exception e, long frameTimestamp) {
                runOnUiThread(() -> handleError(e));
            }
        });
    }
}
```

### Building and Running

#### 1. Configure API URL

In `MainActivity.java`, set your API URL:

```java
private static final String DEFAULT_API_URL = "https://object-detection-api-production.up.railway.app";
```

#### 3. Build and Install

```bash
./gradlew assembleDebug
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Permissions Handling

The app uses EasyPermissions for streamlined permission management:

```java
@AfterPermissionGranted(RC_CAMERA_PERM)
private void requestCameraPermission() {
    String[] perms = {Manifest.permission.CAMERA};
    
    if (EasyPermissions.hasPermissions(this, perms)) {
        startCameraIntent();
    } else {
        EasyPermissions.requestPermissions(this, 
            "Camera permission needed", RC_CAMERA_PERM, perms);
    }
}
```

## Dashboard Portal

### Overview

The web-based dashboard provides real-time monitoring, analytics, and management capabilities for the object detection platform.

### Features

- **Real-time Analytics**: Live metrics and performance monitoring
- **Detection Management**: View, filter, and manage detection history
- **System Monitoring**: Service health and performance tracking
- **Image Processing**: Direct upload and URL-based detection
- **Configuration**: System settings and parameter tuning

### Setup and Installation

#### 1. Install Dependencies

```bash
cd dashboard-portal
npm install
```

#### 2. Configuration

Create a `.env` file:

```env
PORT=3000
API_BASE_URL=http://localhost:8080/api
NODE_ENV=development
```

#### 3. Start Development Server

```bash
npm run dev
```

#### 4. Production Build

```bash
npm start
```

### Dashboard Components

#### Metrics Dashboard

Real-time system metrics with auto-refresh:

```javascript
// Update dashboard metrics every 30 seconds
setInterval(async function() {
    const metrics = await fetchDashboardMetrics();
    updateMetricsDisplay(metrics);
}, 30000);
```

#### Detection Processing

Direct image processing through the dashboard:

```javascript
async function handleFileUpload() {
    const formData = new FormData();
    formData.append('image', file);
    
    const response = await fetch(`${API_BASE_URL}/detect`, {
        method: 'POST',
        body: formData,
        headers: {
            'X-Client-Type': 'Web Portal',
            'X-Device-Info': getClientDeviceInfo()
        }
    });
    
    const result = await response.json();
    displayResults(result);
}
```

#### Analytics Visualization

Chart.js integration for comprehensive analytics:

```javascript
const chart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: chartData.labels,
        datasets: [{
            label: 'API Calls',
            data: chartData.data,
            borderColor: 'rgba(13, 110, 253, 1)',
            tension: 0.3
        }]
    }
});
```

### Customization

#### Themes and Styling

The dashboard uses Bootstrap 5 with custom CSS:

```css
.sidebar {
    min-height: 100vh;
    background-color: #343a40;
}

.card-dashboard:hover {
    transform: translateY(-3px);
    box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
}
```

#### API Integration

Configure API endpoints in `script.js`:

```javascript
const API_BASE_URL = "http://localhost:8080/api";
const DASHBOARD_API_URL = "http://localhost:8080/api/dashboard";
```

## Deployment

### Production Deployment Options

#### Option 1: Docker Compose

Create a `docker-compose.yml`:

```yaml
version: '3.8'
services:
  api:
    build: ./backend-api
    ports:
      - "8080:8080"
    environment:
      - CLOUDINARY_CLOUD_NAME=${CLOUDINARY_CLOUD_NAME}
      - CLOUDINARY_API_KEY=${CLOUDINARY_API_KEY}
      - CLOUDINARY_API_SECRET=${CLOUDINARY_API_SECRET}
      - HUGGINGFACE_API_TOKEN=${HUGGINGFACE_API_TOKEN}
  
  dashboard:
    build: ./dashboard-portal
    ports:
      - "3000:3000"
    depends_on:
      - api
```

Run with:
```bash
docker-compose up -d
```

#### Option 2: Cloud Deployment (Railway)

1. **API Service Deployment**:
   - Connect your GitHub repository to Railway
   - Set environment variables in Railway dashboard
   - Deploy from `backend-api` directory

2. **Dashboard Deployment**:
   - Create a new Railway service
   - Deploy from `dashboard-portal` directory
   - Update API URLs in configuration

#### Option 3: Traditional Server

```bash
# API Service
cd backend-api
./mvnw clean package
java -jar target/backend-api-0.1.0.jar

# Dashboard
cd dashboard-portal
npm install --production
npm start
```

### Environment Configuration

#### Production Environment Variables

```bash
# API Service
export CLOUDINARY_CLOUD_NAME=your_production_cloud_name
export CLOUDINARY_API_KEY=your_production_api_key
export CLOUDINARY_API_SECRET=your_production_api_secret
export HUGGINGFACE_API_TOKEN=your_production_token
export SERVER_PORT=8080

# Dashboard
export PORT=3000
export NODE_ENV=production
export API_BASE_URL=https://object-detection-api-production.up.railway.app/api
```

### Health Checks

The API provides health check endpoints:

```http
GET /api/detect/health
```

Response:
```json
{
  "status": "UP",
  "service": "Object Detection API",
  "version": "1.0.0",
  "objectDetectionService": "UP",
  "dashboardService": "UP"
}
```

## Troubleshooting

### Common Issues

#### API Service Issues

**Problem**: `java.lang.OutOfMemoryError`
```bash
# Solution: Increase JVM memory
java -Xmx2G -jar backend-api-0.1.0.jar
```

**Problem**: Hugging Face API timeouts
```properties
# Solution: Increase timeout in application.properties
huggingface.api.timeout=60000
```

**Problem**: Cloudinary upload failures
```bash
# Solution: Verify credentials and check network
curl -X GET "https://api.cloudinary.com/v1_1/your_cloud_name/usage" \
  -u your_api_key:your_api_secret
```

#### Android SDK Issues

**Problem**: `ImageDetector not initialized`
```java
// Solution: Ensure SDK is initialized before use
@Override
public void onCreate() {
    super.onCreate();
    ImageDetector.init("https://your-api-url.com/");
}
```

**Problem**: Camera permission denied
```xml
<!-- Solution: Add permissions to AndroidManifest.xml -->
<uses-permission android:name="android.permission.CAMERA" />
<uses-feature android:name="android.hardware.camera" android:required="false" />
```

**Problem**: Overlay misalignment
```java
// Solution: Use proper coordinate transformation
overlayView.setDetectionResult(result, 
    originalWidth, originalHeight,
    displayWidth, displayHeight, 
    offsetX, offsetY);
```

#### Dashboard Issues

**Problem**: CORS errors
```javascript
// Solution: Ensure API CORS configuration
@CrossOrigin(origins = {"http://localhost:3000", "https://your-domain.com"})
```

**Problem**: Chart rendering issues
```javascript
// Solution: Ensure Chart.js is loaded before initialization
if (typeof Chart !== 'undefined') {
    initializeCharts();
}
```

### Debug Mode

#### Enable API Debug Logging

```properties
logging.level.com.objectdetection=DEBUG
logging.level.org.springframework.web=DEBUG
```

#### Android SDK Debug Logging

```java
if (BuildConfig.DEBUG) {
    Log.d(TAG, "Detection result: " + result.toString());
}
```

#### Dashboard Debug Mode

```javascript
// Enable console logging
const DEBUG_MODE = true;

if (DEBUG_MODE) {
    console.log('API Response:', response);
}
```

### Performance Optimization

#### API Service

- Use connection pooling for Hugging Face API calls
- Implement response caching for repeated requests
- Optimize image processing pipeline

#### Android SDK

- Implement frame skipping for live detection
- Use background threads for heavy processing
- Optimize overlay drawing performance

#### Dashboard

- Implement data pagination for large datasets
- Use WebSocket for real-time updates
- Optimize chart rendering with data sampling

## Contributing

### Development Workflow

1. **Fork the repository**
2. **Create a feature branch**: `git checkout -b feature/amazing-feature`
3. **Make changes and test thoroughly**
4. **Commit changes**: `git commit -m 'Add amazing feature'`
5. **Push to branch**: `git push origin feature/amazing-feature`
6. **Open a Pull Request**

### Code Style Guidelines

#### Java (API Service & Android SDK)

- Follow Google Java Style Guide
- Use meaningful variable and method names
- Include comprehensive JavaDoc documentation
- Maintain test coverage above 80%

#### JavaScript (Dashboard)

- Use ESLint with Airbnb configuration
- Follow consistent naming conventions
- Include JSDoc comments for complex functions
- Maintain browser compatibility

### Testing

#### API Service Tests

```bash
cd backend-api
./mvnw test
```

#### Android SDK Tests

```bash
cd android-sdk
./gradlew test
```

#### Dashboard Tests

```bash
cd dashboard-portal
npm test
```

### Reporting Issues

When reporting issues, please include:

- **Environment details** (OS, Java version, Android version)
- **Steps to reproduce** the issue
- **Expected vs actual behavior**
- **Relevant logs** and error messages
- **Screenshots** if applicable

### Feature Requests

We welcome feature requests! Please:

- Check existing issues first
- Provide detailed use case descriptions
- Include mockups or examples if applicable
- Consider implementation complexity

---

## License

This project is licensed under the MIT License - see the [LICENSE](https://github.com/EliorMauda/Advanced_Seminar_in_Mobile_Development_2025/blob/master/LICENSE) file for details.

## Support

- **Documentation**: [GitHub Pages](https://EliorMauda.github.io/Advanced_Seminar_in_Mobile_Development_2025/)
- **Issues**: [GitHub Issues](https://github.com/EliorMauda/Advanced_Seminar_in_Mobile_Development_2025/issues)
- **Discussions**: [GitHub Discussions](https://github.com/EliorMauda/Advanced_Seminar_in_Mobile_Development_2025/discussions)

---

**Built with ❤️ by Elior Mauda**
