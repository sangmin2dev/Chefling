package com.example.sungho.chef;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.WindowManager;

import com.example.sungho.chef.databinding.ActivityCookBinding;

public class CookActivity extends AppCompatActivity {
    ActivityCookBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_cook_custom1);
        binding.setActivity(this);


    }
}