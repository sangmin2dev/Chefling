package com.example.sungho.chef;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.sungho.chef.Data.Cooks;
import com.example.sungho.chef.Data.Foods;
import com.example.sungho.chef.Data.MenuData;
import com.example.sungho.chef.Data.Positions;
import com.example.sungho.chef.databinding.ActivityCookModify1Binding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class CookModify1 extends AppCompatActivity {
    //Activity
    ActivityCookModify1Binding binding;
    ExpandableListView test_ExpandableListView;

    // Adapter Data
    MenuData menuData;
    CustomAdapter adapter;
    ArrayList<String> groupListDatas;
    ArrayList<ArrayList<Foods>> childListDatas;
    String activityName;
    int lastClicked = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cook_modify1);
        test_ExpandableListView = binding.expandablelist;

        activityName = this.getClass().getSimpleName().trim();
        menuData = new MenuData();

        // 불러오기
        setData();
    }
    private void setData(){
        // 불러오기 진행 Dialog 보이기
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("데이터 불러오는중...");
        progressDialog.show();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference menuRef = database.getReference("menu");
        // FireBase Event Listner
        menuRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 데이터 받아오기
                groupListDatas = new ArrayList<String>();
                childListDatas = new ArrayList<ArrayList<Foods>>();
                parsing(dataSnapshot);
                progressDialog.dismiss();
                // 어댑터설정, 확장리스트뷰
                adapter = new CustomAdapter(menuData,getApplicationContext(),groupListDatas,childListDatas);
                test_ExpandableListView.setAdapter(adapter);

                test_ExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                        // 선택 한 groupPosition의 펼침/닫힘 상태 체크
                        Boolean isExpand = (!test_ExpandableListView.isGroupExpanded(groupPosition));

                        // 이 전에 열려있던 group 닫기
                        test_ExpandableListView.collapseGroup(lastClicked);
                        if (isExpand) {
                            test_ExpandableListView.expandGroup(groupPosition);
                        }
                        lastClicked = groupPosition;
                        return true;
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CookModify1.this, "정보 불러오기를 실패하였습니다. 다시 실행해주세요.", Toast.LENGTH_SHORT).show();
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

        for(int i = 0; i < menuData.getFoods_type().size(); i ++){
            groupListDatas.add(menuData.getFoods_type().get(i));
        }
        for(int i = 0; i < menuData.getFoods_type().size(); i ++) {
            ArrayList<Foods> foods = new ArrayList<Foods>();
            for (int j = 0; j < menuData.getFoods().size(); j++) {
                // 카테고리별로 분류
                if(menuData.getFoods().get(j).getCategory().equals(menuData.getFoods_type().get(i))){
                    foods.add(menuData.getFoods().get(j));
                }
            }
            childListDatas.add(foods);
        }

        for(int i = 0; i < groupListDatas.size(); i ++) {
            for (int j = 0; j < childListDatas.get(i).size(); j++) {
                Log.d("test : ",i+" , "+j+" ["+groupListDatas.get(i)+"에 있는"+childListDatas.get(i).get(j).getName()+"]");
            }
        }


    }
}