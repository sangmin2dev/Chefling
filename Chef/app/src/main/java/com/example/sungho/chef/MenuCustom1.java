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
import com.example.sungho.chef.databinding.ActivityMenucustom1Binding;

public class MenuCustom1 extends AppCompatActivity {
    ActivityMenucustom1Binding binding;
    ImageButton nextButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_menucustom1);
        binding.setActivity(this);

        nextButton = binding.nextbtn;

        nextButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(),MenuCustom2.class);
                startActivity(intent);
            }
        });
    }
}
