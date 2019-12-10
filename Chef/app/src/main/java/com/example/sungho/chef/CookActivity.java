package com.example.sungho.chef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sungho.chef.Data.Cooks;
import com.example.sungho.chef.Data.Foods;
import com.example.sungho.chef.Data.MenuData;
import com.example.sungho.chef.Data.Positions;
import com.example.sungho.chef.databinding.ActivityCookBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

// 2019 11 26 박성호
// 요리사가 완료한 메뉴 삭제하는 함수 작성해야함

public class CookActivity extends AppCompatActivity {
    ActivityCookBinding binding;

    MenuData menuData;
    TextView nameText;
    TextView positionText;
    Switch blockSwitch;
    LinearLayout foodList;
    int index;

    ArrayList<Qmenu> qmenuList = new ArrayList<Qmenu>();
    Cooks cooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cook);
        binding.setActivity(this);

        nameText = binding.name;
        positionText = binding.position;
        foodList = binding.foodlist;
        blockSwitch = binding.blockSwitch;

        menuData = new MenuData();

        Intent intent = getIntent();
        cooks = (Cooks) intent.getSerializableExtra("cook");
        index = (int) intent.getIntExtra("index", 1);

        nameText.setText(cooks.getName());
        positionText.setText(cooks.getPosition() + "[" + cooks.getAbility() + "]");

        // 요리사큐 현재 요리 받기
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference procRef = database.getReference("processing");

        procRef.child("1/" + index + "/3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("/0").getValue().toString().equals("None")) {
                    Log.d("None?", "None!");
                } else {
                    foodList.removeAllViews();
                    qmenuList.clear();
                    parsingProcessing(dataSnapshot);
                    displayMenu();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        DatabaseReference menuRef = database.getReference("menu");

        menuRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                parsing(dataSnapshot);

                if (menuData.getCooks().get(index).isBreaktime()) {
                    blockSwitch.setChecked(true);
                } else {
                    blockSwitch.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final int childNum = index;
        blockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference menuRef = database.getReference("menu");

                menuRef.child("/cooks/" + childNum + "/breaktime").setValue(isChecked);
            }
        });
    }

    public void completeMenu(Qmenu qmenu, final String completeMenu, int which) {
        //        1. processing 에 있는 음식을 삭제한다.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference processingRef = database.getReference("processing");
        qmenuList.remove(qmenu);
        ArrayList a = new ArrayList();
        for (int i = 0; i < qmenuList.size(); i++) {
            ArrayList arr = new ArrayList();
            arr.add(qmenuList.get(i).getOrderId());
            arr.add(qmenuList.get(i).getFoodId());
            arr.add(qmenuList.get(i).getCategory());

            ArrayList inarr = new ArrayList();
            inarr.add(qmenuList.get(i).getName());
            inarr.add(qmenuList.get(i).getTime());
            arr.add(inarr);
            arr.add(qmenuList.get(i).getType());

            a.add(arr);
        }

        if (a.size() == 0) {
            a.add("None");
        }
        processingRef.child("1/" + index + "/3").setValue(a);

        //        2. 완료한 음식을 order에서 찾아서 요리완료로 변경한다.
        // 현재 완료한 요리의 cookId를 order 에서 찾는다.
        DatabaseReference orderRef = database.getReference("order");
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                parsingOrder(dataSnapshot, completeMenu);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //        3. 그 음식을 served에 쳐넣는다
        DatabaseReference servedRef = database.getReference("served");

        Served served = new Served();
        served.setFoodName(qmenu.getName());
        served.setCookName(nameText.getText().toString());
        served.setFoodId(qmenu.getFoodId());

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String formatDate = sdfNow.format(date);
        served.setCookedTime(formatDate);

        servedRef.push().setValue(served);
    }

    public void displayMenu() {
        for (int i = 0; i < qmenuList.size(); i++) {
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            final Button menuButton = new Button(this);
            menuButton.setLayoutParams(layoutParams);
            menuButton.setBackground(getDrawable(R.drawable.cookingmenubtn));
            menuButton.setText("[" + qmenuList.get(i).getFoodId() + "] " + qmenuList.get(i).getName());
            menuButton.setTextSize(25);
            menuButton.setTextColor(Color.parseColor("#ffffff"));

            final int finalI = i;
            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CookActivity.this);
                    builder.setTitle("메뉴를 완성했습니까?");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {
                            foodList.removeAllViews();
                            completeMenu(qmenuList.get(finalI), qmenuList.get(finalI).getFoodId(), finalI);
                            Toast.makeText(getApplicationContext(), "메뉴를 완료하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                }
            });
            foodList.addView(menuButton);
        }
    }

    public void parsingProcessing(DataSnapshot dataSnapshot) {
        String food_str = "";
        for (DataSnapshot food : dataSnapshot.getChildren()) {      // food 0,1,2...
            food_str = food.getKey();
            Qmenu qmenu = new Qmenu();
            for (DataSnapshot key : food.getChildren()) {             // food key
                qmenu.setMenuUrl(food_str);
                switch (key.getKey()) {
                    case "0":   // order id
                        qmenu.setOrderId(Integer.parseInt(key.getValue().toString()));
                        break;
                    case "1":   // food id
                        qmenu.setFoodId(key.getValue().toString());
                        break;
                    case "2":   // category
                        qmenu.setCategory(key.getValue().toString());
                        break;
                    case "3":
                        for (DataSnapshot inkey : key.getChildren()) {
                            switch (inkey.getKey()) {
                                case "0":   // menu name
                                    qmenu.setName(inkey.getValue().toString());
                                    break;
                                case "1":   // time
                                    qmenu.setTime(Integer.parseInt(inkey.getValue().toString()));
                                    break;
                            }
                        }
                        break;
                    case "4":
                        qmenu.setType(key.getValue().toString());
                        break;
                    case "5":
                        qmenu.setBreakTime((Boolean) key.getValue());
                        break;
                }
            }
            qmenuList.add(qmenu);
        }
    }

    public void parsingOrder(DataSnapshot dataSnapshot, String completeMenu) {
        String order_str = "";
        String foods_str = "";
        String foodIndex_str = "";

        for (DataSnapshot order : dataSnapshot.getChildren()) {       // Order key
            order_str = order.getKey();
            for (DataSnapshot foods : order.getChildren()) {          // foods, orderId, table
                foods_str = foods.getKey();
                if (foods.getKey().equals("foods")) {
                    for (DataSnapshot foodIndex : foods.getChildren()) { // 0,1,2
                        foodIndex_str = foodIndex.getKey();
                        for (DataSnapshot foodKey : foodIndex.getChildren()) {    // foodId
                            if (foodKey.getKey().equals("foodId")) {
                                if (foodKey.getValue().equals(completeMenu)) {
                                    //Log.d("확인", "/"+order_str+"/"+foods_str+"/"+foodIndex_str+"/complete");
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference menuRef = database.getReference("order");
                                    menuRef.child("/" + order_str + "/" + foods_str + "/" + foodIndex_str + "/complete").setValue(true);    // 완료한 요리 완료라고 표시
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    // FireBase >> Android
    public void parsing(DataSnapshot dataSnapshot) {
        for (DataSnapshot key2 : dataSnapshot.getChildren()) {      //[cooks,foods,food_type,positions..]
            if (key2.getKey().equals("cooks")) {
                for (DataSnapshot key3 : key2.getChildren()) {        // 0,1,2..
                    Cooks cooks = new Cooks();
                    for (DataSnapshot data : key3.getChildren()) {    // 요리사
                        switch (data.getKey()) {
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
            }
        }
    }
}

class Qmenu{
    int orderId;
    String foodId;
    String category;
    String type;
    String name;
    int time;
    boolean breakTime;
    String menuUrl;
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getMenuUrl() { return menuUrl; }
    public void setMenuUrl(String menuUrl) { this.menuUrl = menuUrl; }
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public String getFoodId() { return foodId; }
    public void setFoodId(String foodId) { this.foodId = foodId; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getTime() { return time; }
    public void setTime(int time) { this.time = time; }
    public boolean isBreakTime() { return breakTime; }
    public void setBreakTime(boolean breakTime) { this.breakTime = breakTime; }
}
class Served{
    String cookName;
    String foodId;
    String foodName;
    String cookedTime;

    public String getCookName() { return cookName; }
    public void setCookName(String cookName) { this.cookName = cookName; }
    public String getFoodId() { return foodId; }
    public void setFoodId(String foodId) { this.foodId = foodId; }
    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }
    public String getCookedTime() { return cookedTime; }
    public void setCookedTime(String cookedTime) { this.cookedTime = cookedTime; }
}