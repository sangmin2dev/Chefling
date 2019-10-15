package com.example.sungho.chef;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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

    Restaurant rest;

    int count = 0;

    // 입력정보
    ArrayList<String> menuType = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   // 상태바 삭제

        binding = DataBindingUtil.setContentView(this, R.layout.activity_menucustom2);
        binding.setActivity(this);

        Intent intent = getIntent();
        rest = (Restaurant)intent.getSerializableExtra("data");

        container1 = binding.container1;
        container2 = binding.container2;
        container3 = binding.container3;

        preButton = binding.prebtn;
        nextButton = binding.nextbtn;
        addButton = binding.addbtn;
        editText = binding.menuEdit;

        // 추가 버튼
        addButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                // 입력여부 체크
                if(editText.getText().length() == 0){
                    Toast.makeText(getApplicationContext(), "메뉴종류를 입력하세요", Toast.LENGTH_LONG).show();
                }
                // 중복체크
                else if(!checkDuplicate(editText.getText().toString())) {
                    Toast.makeText(getApplicationContext(),"이미 입력한 메뉴종류 입니다",Toast.LENGTH_LONG).show();
                }
                // 메뉴입력
                else if(editText.getText().length() > 0) {
                    menuType.add(editText.getText().toString());
                    rest.addMenuType(editText.getText().toString());    // 메뉴타입 추가
                    displayMenu(menuType.get(menuType.size() - 1));
                }
            }
        });

        // 이전 버튼
        preButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MenuCustom1.class);
                intent.putExtra("data",rest);
                startActivity(intent);
            }
        });

        // 다음 버튼
        nextButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MenuCustom3.class);
                intent.putExtra("data",rest);
                startActivity(intent);
            }
        });
    }

    // 추가한 메뉴종류를 상단에 표시
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
        textView.setGravity(Gravity.CENTER);

        rl.addView(img);
        rl.addView(textView);

        // 줄바꿈
        if(count < 5)
            container1.addView(rl);
        else if(count < 10)
            container2.addView(rl);
        else if(count < 15)
            container3.addView(rl);

        count++;
    }

    // 중복 확인
    public boolean checkDuplicate(String s){
        for(int i = 0; i < menuType.size(); i++){
            if(menuType.get(i).equals(s))
                return false;
        }
        return true;
    }
}
