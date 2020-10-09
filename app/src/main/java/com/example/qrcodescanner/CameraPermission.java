package com.example.qrcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

public class CameraPermission extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_permission);

        checkPermission(Manifest.permission.CAMERA,
                CAMERA_PERMISSION_CODE);


    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(CameraPermission.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(CameraPermission.this,
                    new String[] { permission },
                    requestCode);
        }
        else {
            Toast.makeText(CameraPermission.this,
                    "Permission already granted",
                    Toast.LENGTH_SHORT)
                    .show();
            Intent i = new Intent(CameraPermission.this,Choose.class);
            startActivity(i);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(CameraPermission.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
                Intent i = new Intent(CameraPermission.this,Choose.class);
                startActivity(i);
            }
            else {
                Toast.makeText(CameraPermission.this,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
                Intent i = new Intent(CameraPermission.this,Choose.class);
                startActivity(i);
                finish();
            }
        }

    }

}
