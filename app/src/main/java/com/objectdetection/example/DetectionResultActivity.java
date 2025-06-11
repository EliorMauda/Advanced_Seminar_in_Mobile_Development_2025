package com.objectdetection.example;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.objectdetection.example.adapter.DetectedObjectAdapter;
import com.objectdetection.sdk.DetectorBuilder;
import com.objectdetection.sdk.ImageDetector;
import com.objectdetection.sdk.listener.ImageDetectionListener;
import com.objectdetection.sdk.model.DetectedObject;
import com.objectdetection.sdk.model.DetectionResult;
import com.objectdetection.sdk.view.DetectionOverlayView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DetectionResultActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGE_URI = "extra_image_uri";
    public static final String EXTRA_IMAGE_URL = "extra_image_url";

    private ImageView imageViewResult;
    private DetectionOverlayView detectionOverlay;
    private ProgressBar progressBar;
    private TextView textViewObjectsDetected;
    private TextView textViewProcessingTime;
    private TextView textViewNoObjects;
    private RecyclerView recyclerViewObjects;

    private DetectedObjectAdapter objectAdapter;
    private DetectionResult pendingResult; // Store result until image is loaded

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection_result);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_activity_detection_result);
        }

        // Initialize views
        imageViewResult = findViewById(R.id.imageViewResult);
        detectionOverlay = findViewById(R.id.detectionOverlay);
        progressBar = findViewById(R.id.progressBar);
        textViewObjectsDetected = findViewById(R.id.textViewObjectsDetected);
        textViewProcessingTime = findViewById(R.id.textViewProcessingTime);
        textViewNoObjects = findViewById(R.id.textViewNoObjects);
        recyclerViewObjects = findViewById(R.id.recyclerViewObjects);

        // Set up RecyclerView
        recyclerViewObjects.setLayoutManager(new LinearLayoutManager(this));
        objectAdapter = new DetectedObjectAdapter();
        recyclerViewObjects.setAdapter(objectAdapter);

        // Check SDK initialization
        if (!ImageDetector.isInitialized()) {
            Toast.makeText(this, R.string.error_api_url_not_set, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Process intent data
        processIntent();
    }

    private void processIntent() {
        String imageUriString = getIntent().getStringExtra(EXTRA_IMAGE_URI);
        String imageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);

        if (imageUriString != null) {
            // We got a URI, load and process
            Uri imageUri = Uri.parse(imageUriString);
            loadImage(imageUri);
            processImageUri(imageUri);
        } else if (imageUrl != null) {
            // We got a URL, load and process
            loadImageFromUrl(imageUrl);
            processImageUrl(imageUrl);
        } else {
            Toast.makeText(this, "No image source provided", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadImage(Uri imageUri) {
        showProgress(true);
        Glide.with(this)
                .load(imageUri)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        showProgress(false);
                        Toast.makeText(DetectionResultActivity.this, R.string.error_failed_to_load_image, Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        // Image is loaded, now we can display detection results if they're ready
                        if (pendingResult != null) {
                            displayDetectionResultWhenReady(pendingResult);
                        }
                        return false;
                    }
                })
                .into(imageViewResult);
    }

    private void loadImageFromUrl(String imageUrl) {
        showProgress(true);
        Glide.with(this)
                .load(imageUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        showProgress(false);
                        Toast.makeText(DetectionResultActivity.this, R.string.error_failed_to_load_image, Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        // Image is loaded, now we can display detection results if they're ready
                        if (pendingResult != null) {
                            displayDetectionResultWhenReady(pendingResult);
                        }
                        return false;
                    }
                })
                .into(imageViewResult);
    }

    private void processImageUri(Uri imageUri) {
        showProgress(true);

        DetectorBuilder.with(this)
                .setListener(new ImageDetectionListener() {
                    @Override
                    public void onResult(final DetectionResult result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showProgress(false);
                                if (result.isSuccess()) {
                                    pendingResult = result;
                                    displayDetectionResultWhenReady(result);
                                } else {
                                    handleError(new Exception(result.getError()));
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(final Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showProgress(false);
                                handleError(e);
                            }
                        });
                    }
                })
                .detectFromUri(imageUri);
    }

    private void processImageUrl(String imageUrl) {
        showProgress(true);

        DetectorBuilder.with(this)
                .setListener(new ImageDetectionListener() {
                    @Override
                    public void onResult(final DetectionResult result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showProgress(false);
                                if (result.isSuccess()) {
                                    pendingResult = result;
                                    displayDetectionResultWhenReady(result);
                                } else {
                                    handleError(new Exception(result.getError()));
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(final Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showProgress(false);
                                handleError(e);
                            }
                        });
                    }
                })
                .detectFromUrl(imageUrl);
    }

    private void displayDetectionResultWhenReady(final DetectionResult result) {
        // Wait for the ImageView to be properly laid out
        imageViewResult.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Remove the listener to avoid multiple calls
                imageViewResult.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                // Now display the results with correct coordinates
                displayDetectionResult(result);
            }
        });
    }

    private void displayDetectionResult(DetectionResult result) {
        Drawable drawable = imageViewResult.getDrawable();
        if (drawable == null) {
            return;
        }

        // Get the original image dimensions
        int originalImageWidth = drawable.getIntrinsicWidth();
        int originalImageHeight = drawable.getIntrinsicHeight();

        // Get the ImageView dimensions
        int imageViewWidth = imageViewResult.getWidth();
        int imageViewHeight = imageViewResult.getHeight();

        // Calculate the scale and offset for the image inside the ImageView
        // Since we're using scaleType="fitCenter", we need to calculate the actual displayed image area
        float imageViewAspectRatio = (float) imageViewWidth / imageViewHeight;
        float imageAspectRatio = (float) originalImageWidth / originalImageHeight;

        int displayedImageWidth, displayedImageHeight;
        int offsetX = 0, offsetY = 0;

        if (imageAspectRatio > imageViewAspectRatio) {
            // Image is wider than ImageView - fit to width
            displayedImageWidth = imageViewWidth;
            displayedImageHeight = (int) (imageViewWidth / imageAspectRatio);
            offsetY = (imageViewHeight - displayedImageHeight) / 2;
        } else {
            // Image is taller than ImageView - fit to height
            displayedImageHeight = imageViewHeight;
            displayedImageWidth = (int) (imageViewHeight * imageAspectRatio);
            offsetX = (imageViewWidth - displayedImageWidth) / 2;
        }

        // Set the detection overlay with the correctly calculated dimensions
        detectionOverlay.setDetectionResult(result, originalImageWidth, originalImageHeight,
                displayedImageWidth, displayedImageHeight, offsetX, offsetY);

        // Update texts
        List<DetectedObject> objects = result.getDetectedObjects();
        int objectCount = objects != null ? objects.size() : 0;

        textViewObjectsDetected.setText(getString(R.string.label_objects_detected, objectCount));
        textViewProcessingTime.setText(getString(R.string.label_processing_time, result.getProcessingTimeMs()));

        // Update RecyclerView
        if (objectCount > 0) {
            // Sort objects by confidence (highest first)
            Collections.sort(objects, new Comparator<DetectedObject>() {
                @Override
                public int compare(DetectedObject o1, DetectedObject o2) {
                    return Float.compare(o2.getConfidence(), o1.getConfidence());
                }
            });

            objectAdapter.setObjects(objects);
            textViewNoObjects.setVisibility(View.GONE);
            recyclerViewObjects.setVisibility(View.VISIBLE);
        } else {
            textViewNoObjects.setVisibility(View.VISIBLE);
            recyclerViewObjects.setVisibility(View.GONE);
        }
    }

    private void handleError(Exception e) {
        String errorMessage = e.getMessage();
        if (errorMessage == null) {
            errorMessage = "Unknown error";
        }

        Toast.makeText(this, getString(R.string.error_detection_failed, errorMessage), Toast.LENGTH_LONG).show();
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}