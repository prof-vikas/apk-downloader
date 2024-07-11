package com.sipl.apkdownloader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_STORAGE_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (!checkStoragePermission()) {
            requestStoragePermission();
        }


        TextView app = findViewById(R.id.app);
        app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadApk();
            }
        });


    }

    private boolean checkStoragePermission() {
        int writePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return writePermission == PackageManager.PERMISSION_GRANTED && readPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        if (!checkStoragePermission()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "onRequestPermissionsResult: permission is requested and accpeted");
            } else {
                Log.i(TAG, "onRequestPermissionsResult: permission is denied");
            }
        }
    }


    private void downloadApk() {
        Log.d(TAG, "downloadApk: download apk link is clicked : ");
        getColorCode(getRamdomInt());


//        String fileUrl = "https://yourapi.com/path/to/your/apkfile.apk";
        String fileUrl = "http://10.66.66.90:8080/plas-iportman-rest-repo/apk/download/plas.apk";
        String destinationPath = getExternalFilesDir(null) + File.separator + "downloaded_apk.apk";
        DownloadHelper.downloadFile(fileUrl, destinationPath);


    }

    private int getRamdomInt() {
        Random random = new Random();
        int randomNumber = random.nextInt(5) + 1; // Generates a number between 0 (inclusive) and 5 (exclusive), then adds 1 to shift the range to 1-5
        return randomNumber;
    }

    private int getColorCode(int randomNo) {
        switch (randomNo) {
            case 1:
                return ContextCompat.getColor(this, R.color.yellow);
            case 2:
                return ContextCompat.getColor(this, R.color.blue);
            case 3:
                return ContextCompat.getColor(this, R.color.green);
            case 4:
                return ContextCompat.getColor(this, R.color.pink);
            case 5:
                return ContextCompat.getColor(this, R.color.orange);
            default:
                return ContextCompat.getColor(this, R.color.black);
        }
    }
}