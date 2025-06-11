package com.objectdetection.example;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.preference.PreferenceManager;

import com.google.android.material.button.MaterialButton;
import com.objectdetection.sdk.ImageDetector;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final int RC_CAMERA_PERM = 123;
    private static final int RC_GALLERY_PERM = 124;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;

    private static final String API_URL_PREF_KEY = "api_url";
    private static final String DEFAULT_API_URL = "https://object-detection-api-production.up.railway.app";

    private MaterialButton buttonTakePhoto;
    private MaterialButton buttonGallery;
    private MaterialButton buttonUrl;
    private TextView textViewApiStatus;

    private Uri photoUri;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        buttonTakePhoto = findViewById(R.id.buttonTakePhoto);
        buttonGallery = findViewById(R.id.buttonGallery);
        buttonUrl = findViewById(R.id.buttonUrl);
        textViewApiStatus = findViewById(R.id.textViewApiStatus);
        Button buttonLiveDetection = findViewById(R.id.buttonLiveDetection);

        buttonLiveDetection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLiveDetection();
            }
        });

        // Set up click listeners
        buttonTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission();
            }
        });

        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestGalleryPermission();
            }
        });

        buttonUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUrlInputDialog();
            }
        });

        // Initialize SDK with API URL from preferences
        initializeApiUrl();
    }

    private void startLiveDetection() {
        Intent intent = new Intent(this, LiveDetectionActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateApiStatusText();
    }

    private void initializeApiUrl() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String apiUrl = prefs.getString(API_URL_PREF_KEY, DEFAULT_API_URL);

        try {
            ImageDetector.init(apiUrl);
            updateApiStatusText();
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.error_api_url_not_set), Toast.LENGTH_LONG).show();
        }
    }

    private void updateApiStatusText() {
        if (ImageDetector.isInitialized()) {
            textViewApiStatus.setText("API URL: " + ImageDetector.getApiUrl());
        } else {
            textViewApiStatus.setText("API not initialized");
        }
    }

    @AfterPermissionGranted(RC_CAMERA_PERM)
    private void requestCameraPermission() {
        // For Android 10+ (API 29+), we need separate checks for camera and storage
        String[] perms;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            perms = new String[]{Manifest.permission.CAMERA};
        } else {
            perms = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }

        if (EasyPermissions.hasPermissions(this, perms)) {
            // All permissions granted, proceed with taking picture
            dispatchTakePictureIntent();
        } else {
            // Request the missing permissions
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.permission_rationale_camera),
                    RC_CAMERA_PERM,
                    perms);
        }
    }

    @AfterPermissionGranted(RC_GALLERY_PERM)
    private void requestGalleryPermission() {
        // Different permissions needed based on Android version
        String[] perms;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ uses READ_MEDIA_IMAGES instead of READ_EXTERNAL_STORAGE
            perms = new String[]{Manifest.permission.READ_MEDIA_IMAGES};
        } else {
            perms = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        }

        if (EasyPermissions.hasPermissions(this, perms)) {
            pickImageFromGallery();
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.permission_rationale_storage),
                    RC_GALLERY_PERM,
                    perms);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Error creating image file", Toast.LENGTH_SHORT).show();
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this,
                        getPackageName() + ".fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*"); // This helps ensure we're looking for images
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    private void showUrlInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_enter_url_title);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
        input.setHint(R.string.dialog_enter_url_hint);
        builder.setView(input);

        builder.setPositiveButton(R.string.dialog_btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = input.getText().toString().trim();
                if (url.isEmpty() || !(url.startsWith("http://") || url.startsWith("https://"))) {
                    Toast.makeText(MainActivity.this, R.string.error_invalid_url, Toast.LENGTH_SHORT).show();
                } else {
                    startDetectionWithUrl(url);
                }
            }
        });
        builder.setNegativeButton(R.string.dialog_btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void startDetectionWithUrl(String imageUrl) {
        Intent intent = new Intent(this, DetectionResultActivity.class);
        intent.putExtra(DetectionResultActivity.EXTRA_IMAGE_URL, imageUrl);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                if (photoUri != null) {
                    Intent intent = new Intent(this, DetectionResultActivity.class);
                    intent.putExtra(DetectionResultActivity.EXTRA_IMAGE_URI, photoUri.toString());
                    startActivity(intent);
                }
            } else if (requestCode == REQUEST_PICK_IMAGE) {
                if (data != null && data.getData() != null) {
                    Uri selectedImageUri = data.getData();
                    Intent intent = new Intent(this, DetectionResultActivity.class);
                    intent.putExtra(DetectionResultActivity.EXTRA_IMAGE_URI, selectedImageUri.toString());
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        // Great! Permissions granted
        if (requestCode == RC_CAMERA_PERM) {
            Toast.makeText(this, "Camera permissions granted, taking photo now", Toast.LENGTH_SHORT).show();
            // After permissions are granted, explicitly call the picture intent
            dispatchTakePictureIntent();
        }
        else if (requestCode == RC_GALLERY_PERM) {
            Toast.makeText(this, "Gallery permissions granted, opening gallery now", Toast.LENGTH_SHORT).show();
            // After permissions are granted, explicitly open the gallery
            pickImageFromGallery();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // Log specifics about which permissions were denied
        StringBuilder deniedPerms = new StringBuilder();
        for (String perm : perms) {
            deniedPerms.append(perm).append(", ");
        }

        if (requestCode == RC_CAMERA_PERM) {
            Toast.makeText(this, "Camera permissions denied: " + deniedPerms.toString(), Toast.LENGTH_LONG).show();
        } else if (requestCode == RC_GALLERY_PERM) {
            Toast.makeText(this, "Gallery " + deniedPerms.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            showApiUrlDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showApiUrlDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set API URL");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String currentUrl = prefs.getString(API_URL_PREF_KEY, DEFAULT_API_URL);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
        input.setText(currentUrl);
        builder.setView(input);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = input.getText().toString().trim();
                if (url.isEmpty() || !(url.startsWith("http://") || url.startsWith("https://"))) {
                    Toast.makeText(MainActivity.this, R.string.error_invalid_url, Toast.LENGTH_SHORT).show();
                } else {
                    saveApiUrl(url);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void saveApiUrl(String url) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putString(API_URL_PREF_KEY, url).apply();

        try {
            ImageDetector.init(url);
            updateApiStatusText();
            Toast.makeText(this, "API URL updated", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to initialize SDK with new URL", Toast.LENGTH_SHORT).show();
        }
    }
}