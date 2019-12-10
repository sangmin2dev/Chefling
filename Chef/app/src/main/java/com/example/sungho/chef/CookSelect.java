package com.example.sungho.chef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sungho.chef.Data.Cooks;
import com.example.sungho.chef.Data.Foods;
import com.example.sungho.chef.Data.Positions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CookSelect extends AppCompatActivity {
    ArrayList<Cooks> cookList = new ArrayList<Cooks>();
    LinearLayout list;
    Cooks cooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_select);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        list = findViewById(R.id.cooklist);

        // 요리사큐 현재 요리 받기
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference menuRef = database.getReference("menu");

        menuRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                parsing(dataSnapshot);
                setCookList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setCookList(){
        for(int i = 0; i < cookList.size(); i ++){
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(30,30,0,30);
            LinearLayout cookLayout = new LinearLayout(this);
            cookLayout.setLayoutParams(layoutParams);
            cookLayout.setOrientation(LinearLayout.HORIZONTAL);


            LinearLayout.LayoutParams textLayout =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textLayout.setMargins(40,0,20,0);
            textLayout.gravity = Gravity.CENTER;

            LinearLayout.LayoutParams imageLayout =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            imageLayout.gravity = Gravity.CENTER;
            imageLayout.width = 120;
            imageLayout.height = 120;

            String name = cookList.get(i).getName();
            String position = cookList.get(i).getPosition();
            String ability = cookList.get(i).getAbility() + "";

            // 홍길동
            TextView nameText = new TextView(this);
            nameText.setText(name);
            nameText.setTypeface(null, Typeface.BOLD);
            nameText.setTextSize(20);
            nameText.setLayoutParams(textLayout);

            // 파스타
            final TextView positionText = new TextView(this);
            positionText.setText(position);
            positionText.setTextSize(30);
            positionText.setLayoutParams(textLayout);

            // 2
            final TextView abilityText = new TextView(this);
            abilityText.setText(ability);
            abilityText.setTextSize(20);
            abilityText.setLayoutParams(textLayout);

            ImageButton minus = new ImageButton(this);
            minus.setLayoutParams(imageLayout);
            minus.setImageResource(R.drawable.selectbtn);
            minus.setBackgroundColor(Color.parseColor("#00000000"));
            minus.setScaleType(ImageView.ScaleType.FIT_CENTER);

            cookLayout.addView(nameText);
            cookLayout.addView(positionText);
            cookLayout.addView(abilityText);
            cookLayout.addView(minus);


            final int finalI = i;
            cookLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),CookActivity.class);
                    cooks = cookList.get(finalI);
                    intent.putExtra("cook",cooks);
                    intent.putExtra("index",finalI);
                    startActivity(intent);
                }
            });

            list.addView(cookLayout);
        }
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
                    cookList.add(cooks);
                }
            }
        }
    }
}
