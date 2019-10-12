package com.example.sungho.chef;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
<<<<<<< HEAD
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.sungho.chef.databinding.ActivityCookCustom1Binding;
=======
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sungho.chef.databinding.ActivityCookCustom1Binding;

import java.util.ArrayList;
>>>>>>> 539a3fedcee6cd901a117b5cd751883b04af1702

public class CookCustom1 extends AppCompatActivity {
    ActivityCookCustom1Binding binding;

    LinearLayout container1;
    LinearLayout container2;
    LinearLayout container3;
    EditText nameEdit;
<<<<<<< HEAD
    TableLayout tableLayout;
    ImageButton preButton;
    ImageButton nextButton;
    ImageButton addButton;

    Restaurant rest;
=======
    ImageButton preButton;
    ImageButton nextButton;
    ImageButton addButton;
>>>>>>> 539a3fedcee6cd901a117b5cd751883b04af1702

    int count = 0;
    ArrayList<String> position = new ArrayList<String>();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
=======
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   // 상태바 삭제
>>>>>>> 539a3fedcee6cd901a117b5cd751883b04af1702

        binding = DataBindingUtil.setContentView(this, R.layout.activity_cook_custom1);
        binding.setActivity(this);

<<<<<<< HEAD
        Intent intent = getIntent();
        rest = (Restaurant)intent.getSerializableExtra("data");

=======
>>>>>>> 539a3fedcee6cd901a117b5cd751883b04af1702
        container1 = binding.container1;
        container2 = binding.container2;
        container3 = binding.container3;
        nameEdit = binding.nameEdit;
<<<<<<< HEAD
        tableLayout = binding.table;
=======
>>>>>>> 539a3fedcee6cd901a117b5cd751883b04af1702
        addButton = binding.addbtn;
        preButton = binding.prebtn;
        nextButton = binding.nextbtn;

<<<<<<< HEAD
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
=======
        // 추가 버튼
        addButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                // 입력여부 체크
                if(nameEdit.getText().length() == 0){
                    Toast.makeText(getApplicationContext(), "주방 포지션을 입력하세요", Toast.LENGTH_LONG).show();
                }
                // 중복체크
                else if(!checkDuplicate(nameEdit.getText().toString())) {
                    Toast.makeText(getApplicationContext(),"이미 입력한 포지션 입니다",Toast.LENGTH_LONG).show();
                }
                // 포지션입력
                else if(nameEdit.getText().length() > 0) {
                    position.add(nameEdit.getText().toString());
                    Toast.makeText(getApplicationContext(),nameEdit.getText().toString(),Toast.LENGTH_LONG).show();
                    displayMenu(position.get(position.size() - 1));
                }
            }
        });
>>>>>>> 539a3fedcee6cd901a117b5cd751883b04af1702

        // 이전 버튼
        preButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MenuCustom3.class);
<<<<<<< HEAD
                intent.putExtra("data",rest);
=======
>>>>>>> 539a3fedcee6cd901a117b5cd751883b04af1702
                startActivity(intent);
            }
        });

        // 다음 버튼
        nextButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CookCustom2.class);
<<<<<<< HEAD
                intent.putExtra("data",rest);
                startActivity(intent);
            }
        });
=======
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
        else
            Toast.makeText(getApplicationContext(), "메뉴종류가 너무 많습니다.", Toast.LENGTH_LONG).show();

        count++;
    }

    // 중복 확인
    public boolean checkDuplicate(String s){
        for(int i = 0; i < position.size(); i++){
            if(position.get(i).equals(s))
                return false;
        }
        return true;
>>>>>>> 539a3fedcee6cd901a117b5cd751883b04af1702
    }
}
