package com.example.app_corte1_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.TextView;

import com.example.app_corte1_demo.databinding.ActivityMainBinding;
import android.net.Uri;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'app_corte1_demo' library on application startup.
    static {
        System.loadLibrary("app_corte1_demo");
    }

    private ActivityMainBinding binding;
    private ActivityResultLauncher<String> imagePickerLauncher;
    private Bitmap originalBitmap;
    public native void convertToGray(long inputMat, long outputMat);
    public native void applyExoticGaussian(long inputMat, long outputMat);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!org.opencv.android.OpenCVLoader.initDebug()) {
            System.out.println("OpenCV no cargó");
        } else {
            System.out.println("OpenCV cargado correctamente");
        }

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        binding.imageView.setImageURI(uri);
                        originalBitmap = ((BitmapDrawable) binding.imageView.getDrawable()).getBitmap().copy(Bitmap.Config.ARGB_8888, true);
                    }
                });
                
        binding.btnSelectImage.setOnClickListener(v -> {
            imagePickerLauncher.launch("image/*");
        });

        binding.btnGrayscale.setOnClickListener(v -> {
            if (originalBitmap == null) return;

            Mat inputMat = new Mat();
            Mat outputMat = new Mat();

            Utils.bitmapToMat(originalBitmap, inputMat);

            convertToGray(inputMat.getNativeObjAddr(), outputMat.getNativeObjAddr());

            Bitmap resultBitmap = Bitmap.createBitmap(
                    outputMat.cols(),
                    outputMat.rows(),
                    Bitmap.Config.ARGB_8888
            );

            Utils.matToBitmap(outputMat, resultBitmap);

            binding.imageView.setImageBitmap(resultBitmap);
        });
        binding.btnGaussian.setOnClickListener(v -> {
            if (originalBitmap == null) return;

            Mat inputMat = new Mat();
            Mat outputMat = new Mat();

            Utils.bitmapToMat(originalBitmap, inputMat);

            applyExoticGaussian(inputMat.getNativeObjAddr(), outputMat.getNativeObjAddr());

            Bitmap resultBitmap = Bitmap.createBitmap(
                    outputMat.cols(),
                    outputMat.rows(),
                    Bitmap.Config.ARGB_8888
            );

            Utils.matToBitmap(outputMat, resultBitmap);

            binding.imageView.setImageBitmap(resultBitmap);
        });

        TextView tv = binding.sampleText;
        tv.setText("App Corte 1 Demo");
    }
}