package com.example.sungho.chef;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.sungho.chef.databinding.ActivityModifyBinding;

public class ModifyActivity extends AppCompatActivity {
    ActivityModifyBinding binding;
    ImageButton restBtn;
    ImageButton cookBtn;
    ImageButton menuBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_modify);

        restBtn = binding.restbtn;
        cookBtn = binding.cookbtn;
        menuBtn = binding.menubtn;

        restBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CookModify1.class);
                startActivity(intent);
            }
        });

        cookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CookModify2.class);
                startActivity(intent);
            }
        });

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MenuModify.class);
                startActivity(intent);
            }
        });
    }
}
