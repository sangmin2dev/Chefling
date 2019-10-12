package com.example.sungho.chef;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TabHost;

import com.example.sungho.chef.databinding.ActivityMenuBinding;

public class MenuActivity extends AppCompatActivity {
    ActivityMenuBinding binding;
    TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   // 상태바 삭제

        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu);
        binding.setActivity(this);

        tabHost = binding.tabhost;
        tabHost.setup();

        tabHost.addTab(tabHost.newTabSpec("1").setContent(R.id.tab1).setIndicator("샐러드"));
        tabHost.addTab(tabHost.newTabSpec("2").setContent(R.id.tab2).setIndicator("파스타"));
        tabHost.addTab(tabHost.newTabSpec("3").setContent(R.id.tab3).setIndicator("디저트"));

    }
}
