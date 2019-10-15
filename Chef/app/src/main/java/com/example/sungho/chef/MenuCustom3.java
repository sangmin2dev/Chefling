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

import com.example.sungho.chef.databinding.ActivityMenucustom3Binding;

import java.util.ArrayList;

public class MenuCustom3 extends AppCompatActivity {
    ActivityMenucustom3Binding binding;

    LinearLayout container1;
    LinearLayout container2;
    LinearLayout container3;
    Spinner typeSpiner;
    EditText nameEdit;
    EditText priceEdit;
    EditText infoEdit;
    ImageButton preButton;
    ImageButton nextButton;
    ImageButton addButton;

    Restaurant rest;
    ArrayList<String> menuTypeList;
    ArrayList<String> menu;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   // 상태바 삭제

        binding = DataBindingUtil.setContentView(this, R.layout.activity_menucustom3);
        binding.setActivity(this);

        Intent intent = getIntent();
        rest = (Restaurant)intent.getSerializableExtra("data");

        container1 = binding.container1;
        container2 = binding.container2;
        container3 = binding.container3;
        nameEdit = binding.nameEdit;
        typeSpiner = binding.typeSpinner;
        priceEdit = binding.priceEdit;
        infoEdit = binding.infoEdit;
        addButton = binding.addbtn;
        preButton = binding.prebtn;
        nextButton = binding.nextbtn;

        // 중복체크용
        menu = new ArrayList<String>();

        // 스피터 값 할당
        menuTypeList = new ArrayList<String>();
        for(int i = 0; i < rest.menuTypes.size(); i++){
            menuTypeList.add(rest.menuTypes.get(i).typeName);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                menuTypeList);
        typeSpiner.setAdapter(adapter);
        typeSpiner.setSelection(0);

        // 추가 버튼
        addButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                // 입력여부 체크
                if(nameEdit.getText().length() == 0){
                    Toast.makeText(getApplicationContext(), "메뉴종류를 입력하세요", Toast.LENGTH_LONG).show();
                }
                // 중복체크
                else if(!checkDuplicate(nameEdit.getText().toString())) {
                    Toast.makeText(getApplicationContext(),"이미 입력한 메뉴종류 입니다",Toast.LENGTH_LONG).show();
                }
                // 메뉴입력
                else if(nameEdit.getText().length() > 0) {
                    // 선택한 메뉴타입에 해당 메뉴 추가
                    for(int i = 0; i < rest.menuTypes.size(); i++){
                        if(rest.menuTypes.get(i).typeName.equals(typeSpiner.getSelectedItem().toString()))
                            rest.menuTypes.get(i).AddMenu(
                                    nameEdit.getText().toString(),                      // 이름
                                    typeSpiner.getSelectedItem().toString(),            // 타입
                                    Integer.parseInt(priceEdit.getText().toString()),   // 가격
                                    infoEdit.getText().toString());                     // 정보
                    }
                    menu.add(nameEdit.getText().toString());
                    displayMenu(nameEdit.getText().toString());
                }
            }
        });

        // 이전 버튼
        preButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MenuCustom2.class);
                intent.putExtra("data",rest);
                startActivity(intent);
            }
        });

        // 다음 버튼
        nextButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CookCustom1.class);
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
        for(int i = 0; i < menu.size(); i++){
            if(menu.get(i).equals(s))
                return false;
        }
        return true;
    }
}
