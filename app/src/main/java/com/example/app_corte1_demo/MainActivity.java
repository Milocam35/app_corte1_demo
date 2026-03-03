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
    public native void convertToGray(long inputMat, long outputMat);

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
                    }
                });
                
        binding.btnSelectImage.setOnClickListener(v -> {
            imagePickerLauncher.launch("image/*");
        });

        binding.btnGrayscale.setOnClickListener(v -> {

            Mat inputMat = new Mat();
            Mat outputMat = new Mat();

            Bitmap bitmap = ((BitmapDrawable) binding.imageView.getDrawable()).getBitmap();

            Utils.bitmapToMat(bitmap, inputMat);

            convertToGray(inputMat.getNativeObjAddr(), outputMat.getNativeObjAddr());

            Bitmap resultBitmap = Bitmap.createBitmap(
                    outputMat.cols(),
                    outputMat.rows(),
                    Bitmap.Config.ARGB_8888
            );

            Utils.matToBitmap(outputMat, resultBitmap);

            binding.imageView.setImageBitmap(resultBitmap);
        });
        // Example of a call to a native method
        TextView tv = binding.sampleText;
        tv.setText(stringFromJNI());
    }

    /**
     * A native method that is implemented by the 'app_corte1_demo' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}