package com.example.app_corte1_demo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_corte1_demo.databinding.ActivityMenuBinding;

public class MenuActivity extends AppCompatActivity {

    private ActivityMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnGallery.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });

        binding.btnCamera.setOnClickListener(v -> {
            startActivity(new Intent(this, CameraActivity.class));
        });
    }
}
