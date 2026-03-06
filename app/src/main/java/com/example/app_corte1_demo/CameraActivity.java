package com.example.app_corte1_demo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.app_corte1_demo.databinding.ActivityCameraBinding;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

public class CameraActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    static {
        System.loadLibrary("app_corte1_demo");
    }

    private ActivityCameraBinding binding;
    private boolean cameraRunning = false;
    private boolean filterEnabled = false;

    public native void applySobel(long inputMat, long outputMat);

    private final ActivityResultLauncher<String> cameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) {
                    startCamera();
                } else {
                    Toast.makeText(this, "Se necesita permiso de cámara", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!OpenCVLoader.initDebug()) {
            Toast.makeText(this, "Error al cargar OpenCV", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        binding.cameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_BACK);
        binding.cameraView.setCvCameraViewListener(this);

        binding.btnStartStop.setOnClickListener(v -> {
            if (cameraRunning) {
                stopCamera();
            } else {
                requestCameraAndStart();
            }
        });

        binding.btnFilter.setOnClickListener(v -> {
            filterEnabled = !filterEnabled;
            binding.btnFilter.setText(filterEnabled ? "Quitar Filtro" : "Aplicar Filtro");
        });
    }

    private void requestCameraAndStart() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void startCamera() {
        cameraRunning = true;
        binding.cameraView.setVisibility(View.VISIBLE);
        binding.statusText.setVisibility(View.GONE);
        binding.cameraView.setCameraPermissionGranted();
        binding.cameraView.enableView();
        binding.btnStartStop.setText("Detener");
        binding.btnFilter.setEnabled(true);
    }

    private void stopCamera() {
        cameraRunning = false;
        filterEnabled = false;
        binding.cameraView.disableView();
        binding.cameraView.setVisibility(View.GONE);
        binding.statusText.setVisibility(View.VISIBLE);
        binding.btnStartStop.setText("Iniciar");
        binding.btnFilter.setText("Aplicar Filtro");
        binding.btnFilter.setEnabled(false);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
    }

    @Override
    public void onCameraViewStopped() {
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat rgba = inputFrame.rgba();
        if (filterEnabled) {
            Mat output = new Mat();
            applySobel(rgba.getNativeObjAddr(), output.getNativeObjAddr());
            return output;
        }
        return rgba;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraRunning) {
            binding.cameraView.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cameraRunning) {
            binding.cameraView.enableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.cameraView.disableView();
    }
}
