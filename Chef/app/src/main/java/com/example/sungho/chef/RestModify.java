package com.example.sungho.chef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sungho.chef.Data.Foods;
import com.example.sungho.chef.databinding.ActivityRestModifyBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RestModify extends AppCompatActivity {
    ActivityRestModifyBinding binding;

    String name;
    String info;

    EditText nameEdit;
    EditText infoEdit;
    ImageButton okBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rest_modify);

        nameEdit = binding.nameEdit;
        infoEdit = binding.infoEdit;
        okBtn = binding.okBtn;

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference menuRef = database.getReference("menu");

        // FireBase Event Listner
        menuRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 데이터 받아오기
                parsing(dataSnapshot);
                // 데이터 editText에 표시
                nameEdit.setText(name);
                infoEdit.setText(info);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RestModify.this, "정보 불러오기를 실패하였습니다. 다시 실행해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference menuRef = database.getReference("menu");

                Map<String,Object> taskMap = new HashMap<String, Object>();
                taskMap.put("rest_info/name",nameEdit.getText().toString());
                taskMap.put("rest_info/description", infoEdit.getText().toString());

                menuRef.updateChildren(taskMap);

                menuRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("firebaseTAG","success :)");
                        Intent intent = new Intent(getApplicationContext(),ModifyActivity.class);
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

    // FireBase >> Android
    public void parsing(DataSnapshot dataSnapshot) {
        for (DataSnapshot key2 : dataSnapshot.getChildren()) {              //foods
            if (key2.getKey().equals("rest_info")) {
                for (DataSnapshot data : key2.getChildren()) {   // 속성
                    if (data.getKey().equals("name")) {
                        name = data.getValue().toString();
                    }
                    else if (data.getKey().equals("description")) {
                        info = data.getValue().toString();
                    }
                }
            }
        }
    }
}