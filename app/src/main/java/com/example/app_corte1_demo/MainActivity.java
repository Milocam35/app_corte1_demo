package com.example.app_corte1_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.app_corte1_demo.databinding.ActivityMainBinding;
import android.net.Uri;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'app_corte1_demo' library on application startup.
    static {
        System.loadLibrary("app_corte1_demo");
    }

    private ActivityMainBinding binding;
    private ActivityResultLauncher<String> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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