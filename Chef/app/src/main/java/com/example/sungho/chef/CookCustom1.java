package com.example.sungho.chef;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.sungho.chef.databinding.ActivityCookCustom1Binding;

public class CookCustom1 extends AppCompatActivity {
    ActivityCookCustom1Binding binding;

    LinearLayout container1;
    LinearLayout container2;
    LinearLayout container3;
    EditText nameEdit;
    TableLayout tableLayout;
    ImageButton preButton;
    ImageButton nextButton;
    ImageButton addButton;

    Restaurant rest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_cook_custom1);
        binding.setActivity(this);

        Intent intent = getIntent();
        rest = (Restaurant)intent.getSerializableExtra("data");

        container1 = binding.container1;
        container2 = binding.container2;
        container3 = binding.container3;
        nameEdit = binding.nameEdit;
        tableLayout = binding.table;
        addButton = binding.addbtn;
        preButton = binding.prebtn;
        nextButton = binding.nextbtn;

        // 테이블 레이아웃, 체크박스 생성
        int r = 2;
        int c = rest.menuTypes.size();
        TableRow.LayoutParams rowLayout = new TableRow.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.MATCH_PARENT);
        TableRow row[] = new TableRow[r];       //테이블 Row(2) 생성
        CheckBox checkBox[] = new CheckBox[c];  //체크박스 생성

        //Row
        for(int i = 0; i < r; i++){
            row[i] = new TableRow(this);
        }

        for(int i = 0; i < c; i ++){
            checkBox[i] = new CheckBox(this);
            checkBox[i].setText(rest.menuTypes.get(i).typeName);
            checkBox[i].setTextSize(15);
            checkBox[i].setTextColor(Color.BLACK);

            row[i % 2].addView(checkBox[i]);
        }

        for(int i = 0; i < r; i++){
            tableLayout.addView(row[i],rowLayout);
        }

        // 이전 버튼
        preButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MenuCustom3.class);
                intent.putExtra("data",rest);
                startActivity(intent);
            }
        });

        // 다음 버튼
        nextButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CookCustom2.class);
                intent.putExtra("data",rest);
                startActivity(intent);
            }
        });
    }
}
