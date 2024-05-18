package com.example.hawkhack;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 1;
    private TextView statusTextView;
    private TextView locationTextView;
    private Button toggleButton;
    private boolean isCollecting = false;
    private LocationReceiver locationReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusTextView = findViewById(R.id.statusTextView);
        locationTextView = findViewById(R.id.locationTextView);
        toggleButton = findViewById(R.id.toggleButton);

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCollecting) {
                    stopCollecting();
                } else {
                    if (checkPermissions()) {
                        startCollecting();
                    }
                }
            }
        });

        if (!checkPermissions()) {
            requestPermissions();
        }

        locationReceiver = new LocationReceiver(locationTextView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(locationReceiver, new IntentFilter("com.example.hawkhack.LOCATION_UPDATE"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(locationReceiver);
    }

    private boolean checkPermissions() {
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        return locationPermission == PackageManager.PERMISSION_GRANTED && cameraPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, REQUEST_PERMISSIONS);
    }

    private void startCollecting() {
        isCollecting = true;
        statusTextView.setText("Status: Collecting");
        toggleButton.setText("Stop Collecting");

        Intent intent = new Intent(this, DataCollectionService.class);
        startService(intent);
    }

    private void stopCollecting() {
        isCollecting = false;
        statusTextView.setText("Status: Not Collecting");
        toggleButton.setText("Start Collecting");

        Intent intent = new Intent(this, DataCollectionService.class);
        stopService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCollecting();
            } else {
                // Handle permission denial
            }
        }
    }
}
