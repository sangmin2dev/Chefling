package com.example.sungho.chef;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.sungho.chef.Data.Cooks;
import com.example.sungho.chef.Data.Foods;
import com.example.sungho.chef.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ImageButton ownerButton;
    ImageButton menuButton;
    //Firebase Test 버튼
    Button fireBaseTestBtn;
    Button fireBaseTestBtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);

        ownerButton = binding.ownerBtn;
        menuButton = binding.menuBtn;

        //*********파이어베이스 테스트용 객체
        fireBaseTestBtn = (Button) findViewById(R.id.fb_test_btn1);
        fireBaseTestBtn2 = (Button) findViewById(R.id.fb_test_btn2);

        //**********

        // ******파이어베이스 송수신 함수 (테스트용)
        fireBaseTestBtn2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference menuRef = database.getReference("menu");

                Foods food = new Foods();
                food.setCategory("Pasta");
                food.setCooking_time(30);
                food.setDescription("Deiliciout Pasta");
                food.setName("AlioOlio");
                food.setPrice(15000);
                food.setSold_out(false);
                food.setUrl("./pic/steak/Alio Olio");
                menuRef.child("foods").push().setValue(food);
                menuRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
                        Log.d("firebase_T", "Value is: " + value);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("firebase_T", "FB_fail", error.toException());
                    }
                });
            }
        });
        fireBaseTestBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference menuRef = database.getReference("menu");
                //menuRef.child("cooks").setValue("cook1");
               // Map<String, Object> childUpdates = new HashMap<>();
                //childUpdates.put("menu/cooks/","cook1");
                Cooks cook1 = new Cooks();
                cook1.setAbility(3);
                cook1.setName("JSW");
                cook1.setPosition("Pasta");
                cook1.setBreaktime("1130~1230");
                menuRef.child("cooks").push().setValue(cook1);
                menuRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
                        Log.d("firebase_T", "Value is: " + value);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("firebase_T", "FB_fail", error.toException());
                    }
                });

            }
        });
        // ******파이어베이스 송수신 함수 (테스트용)

        // 점주 페이지로 이동
        ownerButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(),MenuCustom1.class);
                startActivity(intent);
            }
        });

        // 메뉴판 페이지로 이동
        menuButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MenuActivity.class);
                startActivity(intent);
            }
        });

        // 직원 페이지로 이동


    }


}
