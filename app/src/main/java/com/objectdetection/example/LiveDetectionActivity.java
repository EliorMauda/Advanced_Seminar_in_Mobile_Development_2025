package com.objectdetection.example;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.objectdetection.sdk.ImageDetector;
import com.objectdetection.sdk.listener.LiveDetectionListener;
import com.objectdetection.sdk.model.DetectionResult;
import com.objectdetection.sdk.view.DetectionOverlayView;

import java.util.Locale;

public class LiveDetectionActivity extends AppCompatActivity {
    private static final String TAG = "LiveDetectionActivity";
    private static final int REQUEST_CAMERA_PERMISSION = 10;

    private PreviewView previewView;
    private DetectionOverlayView overlayView;
    private TextView statusTextView;
    private Button switchCameraButton;

    private boolean isFrontCamera = false;
    private boolean isDetectionRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_detection);

        // Initialize views
        previewView = findViewById(R.id.previewView);
        overlayView = findViewById(R.id.overlayView);
        statusTextView = findViewById(R.id.statusTextView);
        switchCameraButton = findViewById(R.id.switchCameraButton);

        Button toggleDetectionButton = findViewById(R.id.toggleDetectionButton);
        toggleDetectionButton.setOnClickListener(v -> toggleDetection());

        switchCameraButton.setOnClickListener(v -> switchCamera());

        // Check API URL configuration
        if (!ImageDetector.isInitialized()) {
            Toast.makeText(this, R.string.error_api_url_not_set, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Check for camera permission
        if (hasCameraPermission()) {
            startDetection();
        } else {
            requestCameraPermission();
        }
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        } else {
            startDetection(); // Permission already granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startDetection();
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void startDetection() {
        if (isDetectionRunning) return;

        statusTextView.setText("Starting detection...");

        LiveDetectionListener listener = new LiveDetectionListener() {
            @Override
            public void onDetectionResult(DetectionResult result, long frameTimestamp) {
                runOnUiThread(() -> {
                    if (result.isSuccess()) {
                        // Get the preview view dimensions
                        int previewWidth = previewView.getWidth();
                        int previewHeight = previewView.getHeight();

                        // Get the actual camera frame dimensions from the result
                        // If your SDK provides frame dimensions, use those. Otherwise, use common camera resolutions
                        Size frameSize = getCameraFrameSize(result);
                        int frameWidth = frameSize.getWidth();
                        int frameHeight = frameSize.getHeight();

                        // Calculate scale and offset for proper coordinate transformation
                        float previewAspectRatio = (float) previewWidth / previewHeight;
                        float frameAspectRatio = (float) frameWidth / frameHeight;

                        int displayedFrameWidth, displayedFrameHeight;
                        int offsetX = 0, offsetY = 0;

                        if (frameAspectRatio > previewAspectRatio) {
                            // Frame is wider than preview - fit to width
                            displayedFrameWidth = previewWidth;
                            displayedFrameHeight = (int) (previewWidth / frameAspectRatio);
                            offsetY = (previewHeight - displayedFrameHeight) / 2;
                        } else {
                            // Frame is taller than preview - fit to height
                            displayedFrameHeight = previewHeight;
                            displayedFrameWidth = (int) (previewHeight * frameAspectRatio);
                            offsetX = (previewWidth - displayedFrameWidth) / 2;
                        }

                        // Update overlay with proper coordinate transformation
                        overlayView.setDetectionResult(result, frameWidth, frameHeight,
                                displayedFrameWidth, displayedFrameHeight,
                                offsetX, offsetY);

                        // Update status text
                        int objectCount = result.getDetectedObjects() != null ?
                                result.getDetectedObjects().size() : 0;
                        statusTextView.setText(String.format(Locale.US,
                                "Detected %d objects (%.0f ms)",
                                objectCount,
                                (float) result.getProcessingTimeMs()));
                    } else {
                        statusTextView.setText("Detection error: " + result.getError());
                    }
                });
            }

            @Override
            public void onError(Exception e, long frameTimestamp) {
                runOnUiThread(() -> {
                    statusTextView.setText("Error: " + e.getMessage());
                    Log.e(TAG, "Detection error", e);
                });
            }
        };

        if (isFrontCamera) {
            ImageDetector.startLiveDetectionFrontCamera(this, previewView, listener);
        } else {
            ImageDetector.startLiveDetection(this, previewView, listener);
        }

        isDetectionRunning = true;
    }

    /**
     * Gets the camera frame size from the detection result.
     * If your SDK provides frame dimensions in the result, use those.
     * Otherwise, use common camera resolutions as fallback.
     */
    private Size getCameraFrameSize(DetectionResult result) {
        // Check if your SDK provides frame dimensions
        // This is just an example - replace with actual method from your SDK
        // if (result.getFrameWidth() > 0 && result.getFrameHeight() > 0) {
        //     return new Size(result.getFrameWidth(), result.getFrameHeight());
        // }

        // Fallback to common camera resolutions
        // You might need to adjust these based on your camera configuration
        return new Size(1920, 1080); // Common Full HD resolution

        // Alternative fallback resolutions:
        // return new Size(1280, 720); // HD
        // return new Size(640, 480);  // VGA
    }

    private void stopDetection() {
        if (!isDetectionRunning) return;

        ImageDetector.stopLiveDetection();
        overlayView.clearDetections();
        statusTextView.setText("Detection stopped");
        isDetectionRunning = false;
    }

    private void toggleDetection() {
        if (isDetectionRunning) {
            stopDetection();
        } else {
            startDetection();
        }
    }

    private void switchCamera() {
        isFrontCamera = !isFrontCamera;
        stopDetection();
        startDetection();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopDetection();
    }
}