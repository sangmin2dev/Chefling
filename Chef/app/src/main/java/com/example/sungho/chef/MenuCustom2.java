package com.example.sungho.chef;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sungho.chef.databinding.ActivityMenucustom2Binding;

import java.util.ArrayList;

public class MenuCustom2 extends AppCompatActivity {
    ActivityMenucustom2Binding binding;

    LinearLayout container1;
    LinearLayout container2;
    LinearLayout container3;


    ImageButton preButton;
    ImageButton nextButton;
    ImageButton addButton;
    EditText editText;

    int count = 0;


    ArrayList<String> menuType = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);



        binding = DataBindingUtil.setContentView(this, R.layout.activity_menucustom2);
        binding.setActivity(this);

        container1 = binding.container1;
        container2 = binding.container2;
        container3 = binding.container3;

        preButton = binding.prebtn;
        nextButton = binding.nextbtn;
        addButton = binding.addbtn;
        editText = binding.editmenu;

        // 추가 버튼
        addButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                // editText 입력시 메뉴종류 추가, 아닐시 토스트 메시지 출력
                if(editText.getText().toString().length() != 0) {
                    menuType.add(editText.getText().toString());
                    displayMenu(menuType.get(menuType.size() - 1));
                }
                else {
                    Toast.makeText(getApplicationContext(), "메뉴종류를 입력하세요", Toast.LENGTH_LONG).show();
                }
            }
        });

        // 이전 버튼
        preButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MenuCustom1.class);
                startActivity(intent);
            }
        });

        // 다음 버튼
        nextButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                //Intent intent = new Intent(getApplicationContext(),MenuCustom3.class);
                //startActivity(intent);
            }
        });


    }

    public void displayMenu(String s){
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        RelativeLayout rl = new RelativeLayout(this);
        rl.setLayoutParams(layoutParams);

        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.menutype);
        img.setLayoutParams(layoutParams);
        img.setId(count);

        TextView textView = new TextView(this);
        textView.setText(s);
        textView.setTextSize(15);
        textView.setTextColor(Color.WHITE);
        textView.setLayoutParams(layoutParams);
        textView.setTextAlignment(img.getId());
        textView.setGravity(Gravity.CENTER);

        rl.addView(img);
        rl.addView(textView);
        if(count < 6)
            container1.addView(rl);
        else if(count < 12)
            container2.addView(rl);
        else
            container3.addView(rl);

        count++;
    }
}
