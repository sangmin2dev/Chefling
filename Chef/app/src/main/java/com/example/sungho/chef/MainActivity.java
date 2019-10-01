package com.example.sungho.chef;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.sungho.chef.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ImageButton ownerButton;
    ImageButton menuButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);

        ownerButton = binding.ownerBtn;
        menuButton = binding.menuBtn;

        // 점주 페이지로 이동
        ownerButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(),MenuCustom1.class);
                startActivity(intent);
            }
        });

        // 메뉴판 페이지로 이동
        menuButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
            }
        });

        // 직원 페이지로 이동


    }


}
