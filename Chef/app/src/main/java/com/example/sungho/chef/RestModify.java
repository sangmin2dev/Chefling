package com.example.sungho.chef;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sungho.chef.Data.Foods;
import com.example.sungho.chef.databinding.ActivityRestModifyBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RestModify extends AppCompatActivity {
    ActivityRestModifyBinding binding;

    String name;
    String info;

    EditText nameEdit;
    EditText infoEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rest_modify);

        nameEdit = binding.nameEdit;
        infoEdit = binding.infoEdit;

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference menuRef = database.getReference("menu");

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
                Toast.makeText(RestModify.this, "메뉴정보 불러오기를 실패하였습니다. 다시 실행해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // FireBase >> Android
    public void parsing(DataSnapshot dataSnapshot) {
        for (DataSnapshot key : dataSnapshot.getChildren()) {
            for (DataSnapshot key2 : key.getChildren()) {              //foods
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
}