package com.example.sungho.chef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.sungho.chef.Data.Cooks;
import com.example.sungho.chef.Data.Foods;
import com.example.sungho.chef.Data.MenuData;
import com.example.sungho.chef.Data.Positions;
import com.example.sungho.chef.databinding.ActivityCookModify2Binding;
import com.example.sungho.chef.databinding.ActivityMenuModifyBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuModify extends AppCompatActivity {
    ActivityMenuModifyBinding binding;
    MenuData menuData;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu_modify);
        linearLayout = binding.container;
        menuData = new MenuData();
        setData();
    }

    // 각 요리사들에 맞는 뷰를 생성
    private void setActivity(){
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams layoutParams2 =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams2.setMargins(40,40,0,40);

        for(int i = 0; i < menuData.getFoods().size(); i++){
            LinearLayout lin = new LinearLayout(MenuModify.this);
            lin.setLayoutParams(layoutParams2);
            lin.setOrientation(LinearLayout.HORIZONTAL);

            // 이름 텍스트뷰
            final TextView foodName = new TextView(MenuModify.this);
            foodName.setLayoutParams(layoutParams);
            foodName.setText(menuData.getFoods().get(i).getName());
            foodName.setTextSize(20);
            foodName.setTextColor(Color.BLACK);
            foodName.setTypeface(null, Typeface.BOLD);

            // 포지션 텍스트뷰
            final TextView categoryName = new TextView(MenuModify.this);
            categoryName.setLayoutParams(layoutParams);
            categoryName.setText("   [ "+menuData.getFoods().get(i).getCategory()+" ]    Sold Out ");
            categoryName.setTextSize(20);
            categoryName.setTextColor(Color.BLACK);

            final Switch switchBtn = new Switch(MenuModify.this);

            if(menuData.getFoods().get(i).isSold_out()){
                switchBtn.setChecked(true);
            }else{
                switchBtn.setChecked(false);
            }

            final int childNum = i;
            switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference menuRef = database.getReference("menu");

                    menuRef.child("/foods/"+childNum+"/sold_out").setValue(isChecked);
                }
            });

            lin.addView(foodName);
            lin.addView(categoryName);
            lin.addView(switchBtn);
            linearLayout.addView(lin);
        }
    }

    private void setData(){
        // 불러오기 진행 Dialog 보이기
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("데이터 불러오는중...");
        progressDialog.show();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference menuRef = database.getReference("menu");
        // FireBase Event Listner
        menuRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                parsing(dataSnapshot);
                progressDialog.dismiss();
                setActivity();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // FireBase >> Android
    public void parsing(DataSnapshot dataSnapshot) {
        for (DataSnapshot key2 : dataSnapshot.getChildren()) {      //[cooks,foods,food_type,positions..]
            if(key2.getKey().equals("cooks")){
                for(DataSnapshot key3 : key2.getChildren()){        // 0,1,2..
                    Cooks cooks = new Cooks();
                    for(DataSnapshot data : key3.getChildren()){    // 요리사
                        switch (data.getKey()){
                            case "name":
                                cooks.setName(data.getValue().toString());
                                break;
                            case "ability":
                                cooks.setAbility(Integer.parseInt(data.getValue().toString()));
                                break;
                            case "position":
                                cooks.setPosition(data.getValue().toString());
                                break;
                            case "breaktime":
                                cooks.setBreaktime(Boolean.parseBoolean(data.getValue().toString()));
                                break;
                        }
                    }
                    menuData.getCooks().add(cooks);
                }
            }else if(key2.getKey().equals("foods")){
                for(DataSnapshot key3 : key2.getChildren()){        // 0,1,2..
                    Foods foods = new Foods();
                    for(DataSnapshot data : key3.getChildren()){    // 음식
                        switch (data.getKey()){
                            case "name":
                                foods.setName(data.getValue().toString());
                                break;
                            case "cooking_time":
                                foods.setCooking_time(Integer.parseInt(data.getValue().toString()));
                                break;
                            case "description":
                                foods.setDescription(data.getValue().toString());
                                break;
                            case "price":
                                foods.setPrice(Integer.parseInt(data.getValue().toString()));
                                break;
                            case "sold_out":
                                foods.setSold_out(Boolean.parseBoolean(data.getValue().toString()));
                                break;
                            case "category":
                                foods.setCategory(data.getValue().toString());
                                break;
                        }
                    }
                    menuData.getFoods().add(foods);
                }
            }
            else if(key2.getKey().equals("foods_type")){            // 메뉴 타입
                for(DataSnapshot key3 : key2.getChildren()) {        //0,1,2
                    String type = new String();
                    type = key3.getValue().toString();
                    menuData.getFoods_type().add(type);
                }
            }else if (key2.getKey().equals("positions")) {
                for(DataSnapshot key3 : key2.getChildren()){        //0,1,2
                    Positions pos = new Positions();
                    for (DataSnapshot data : key3.getChildren()){      // 속성
                        if (data.getKey().equals("name")) {
                            pos.setName(data.getValue().toString());
                        }
                        else if (data.getKey().equals("size")) {
                            pos.setSize(Integer.parseInt(data.getValue().toString()));
                        }
                    }
                    menuData.getPositions().add(pos);
                }
            }
        }
    }
}
