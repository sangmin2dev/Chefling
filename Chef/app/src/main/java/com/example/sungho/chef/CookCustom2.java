package com.example.sungho.chef;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.sungho.chef.databinding.ActivityCookCustom1Binding;
import com.example.sungho.chef.databinding.ActivityCookCustom2Binding;

public class CookCustom2 extends AppCompatActivity {
    ActivityCookCustom2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   // 상태바 삭제

        binding = DataBindingUtil.setContentView(this, R.layout.activity_cook_custom2);
    }
}
