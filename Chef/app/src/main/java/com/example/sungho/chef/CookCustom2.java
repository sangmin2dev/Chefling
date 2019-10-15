package com.example.sungho.chef;

import android.content.Intent;
import android.graphics.Color;
import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.sungho.chef.databinding.ActivityCookCustom2Binding;

import java.util.ArrayList;

public class CookCustom2 extends AppCompatActivity {
    ActivityCookCustom2Binding binding;

    LinearLayout container1;
    LinearLayout container2;
    LinearLayout container3;
    Spinner positionSpiner;
    EditText nameEdit;
    ImageButton preButton;
    ImageButton nextButton;
    ImageButton addButton;

    Restaurant rest;
    ArrayList<String> positionList;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   // 상태바 삭제

        binding = DataBindingUtil.setContentView(this, R.layout.activity_cook_custom2);
        binding.setActivity(this);

        container1 = binding.container1;
        container2 = binding.container2;
        container3 = binding.container3;
        nameEdit = binding.nameEdit;
        positionSpiner = binding.positionSpiner;
        addButton = binding.addbtn;
        preButton = binding.prebtn;
        nextButton = binding.nextbtn;

        Intent intent = getIntent();
        rest = (Restaurant)intent.getSerializableExtra("data");

        // 스피터 값 할당
        positionList = new ArrayList<String>();
        for(int i = 0; i < rest.positions.size(); i++){
            positionList.add(rest.positions.get(i).name);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                positionList);
        positionSpiner.setAdapter(adapter);
        positionSpiner.setSelection(0);

        // 추가 버튼
        addButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                // 입력여부 체크
                if(nameEdit.getText().length() == 0){
                    Toast.makeText(getApplicationContext(), "직원이름을 입력하세요", Toast.LENGTH_LONG).show();
                }
                // 직원입력
                else if(nameEdit.getText().length() > 0) {
                    // 선택한 포지션에 해당 요리사 추가
                    for(int i = 0; i < rest.positions.size(); i++){
                        if(rest.positions.get(i).name.equals(positionSpiner.getSelectedItem().toString()))
                            rest.positions.get(i).addCook(nameEdit.getText().toString());           //요리사 이름
                    }
                    displayMenu(nameEdit.getText().toString());

                }
            }
        });

        // 이전 버튼
        preButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CookCustom1.class);
                intent.putExtra("data",rest);
                startActivity(intent);
            }
        });

        // 다음 버튼
        nextButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MenuActivity.class);
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
}

// fire base
