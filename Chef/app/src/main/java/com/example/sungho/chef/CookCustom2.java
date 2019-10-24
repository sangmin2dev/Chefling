package com.example.sungho.chef;

import android.content.Intent;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
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

import com.example.sungho.chef.Data.Cooks;
import com.example.sungho.chef.Data.Foods;
import com.example.sungho.chef.Data.MenuData;
import com.example.sungho.chef.Data.RestaurantInfo;
import com.example.sungho.chef.databinding.ActivityCookCustom2Binding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.sql.Ref;
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
    ArrayList<Cooks> cooks = new ArrayList<Cooks>();
    ArrayList<Foods> foods = new ArrayList<Foods>();

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

        /**
         * 데이터 종합해서 파이어베이스 전송!
         * 테스트 완료 by JSW 2019-10-24
         * */

        // 파이어베이스 전송 버튼 (이미지 변화 필요)
        nextButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference menuRef = database.getReference("menu");

                dataPacking();
                MenuData menuData = new MenuData();
                menuData.setCooks(cooks);
                menuData.setFoods(foods);
                RestaurantInfo rest_info = new RestaurantInfo();
                rest_info.setName(rest.getName());
                rest_info.setDescription(rest.getInfo());
                menuData.setRest_info(rest_info);
                menuRef.push().setValue(menuData);
                menuRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("firebaseTAG","success :)");
                        //전송후 메뉴액티비티로 이동
                        Intent intent = new Intent(getApplicationContext(),MenuActivity.class);
                        intent.putExtra("data",rest);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("firebaseTAG","Failed :(");
                    }
                });



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


    public void dataPacking(){
        // 1. 입력된 요리사들(Cooks)의 Array List
        for(int i = 0; i < rest.positions.size(); i++) {                    // i : position
            Position position = rest.positions.get(i);
            for(int j = 0; j < position.getCooks().size(); j++) {           // j : position 에 있는 Cook
                Cooks c = new Cooks();
                c.setName(position.getCooks().get(j).getName());
                c.setPosition(position.getName());
                c.setAbility(position.getSize());
                c.setBreaktime(false);
                cooks.add(c);
            }
        }

        // 2. 입력된 메뉴들(Foods)의 Array List
        for(int i = 0; i < rest.menuTypes.size(); i++) {                    // i : menuType
            MenuType type = rest.menuTypes.get(i);
            for(int j = 0; j < type.getMenus().size(); j++) {               // j : menuType 에 있는 menu
                Foods f = new Foods();
                f.setName(type.getMenus().get(j).getName());
                f.setCategory(type.getTypeName());
                f.setCooking_time(type.getMenus().get(j).getTime());
                f.setPrice(type.getMenus().get(j).getPrice());
                f.setDescription(type.getMenus().get(j).getInfo());
                f.setSold_out(false);
                foods.add(f);
            }
        }
    }
}
