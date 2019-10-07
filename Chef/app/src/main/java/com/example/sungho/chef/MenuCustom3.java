package com.example.sungho.chef;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.sungho.chef.databinding.ActivityMenucustom3Binding;

import java.util.ArrayList;

public class MenuCustom3 extends AppCompatActivity {
    ActivityMenucustom3Binding binding;

    LinearLayout container1;
    LinearLayout container2;
    LinearLayout container3;
    Spinner typeSpiner;
    EditText nameEdit;
    EditText costEdit;
    EditText infoEdit;
    ImageButton preButton;
    ImageButton nextButton;
    ImageButton addButton;

    Restaurant rest;
    ArrayList<String> menuTypeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   // 상태바 삭제

        binding = DataBindingUtil.setContentView(this, R.layout.activity_menucustom3);
        binding.setActivity(this);

        Intent intent = getIntent();
        rest = (Restaurant)intent.getSerializableExtra("레스토랑");

        container1 = binding.container1;
        container2 = binding.container2;
        container3 = binding.container3;
        nameEdit = binding.nameEdit;
        typeSpiner = binding.typeSpinner;
        costEdit = binding.costEdit;
        infoEdit = binding.infoEdit;
        addButton = binding.addbtn;
        preButton = binding.prebtn;
        nextButton = binding.nextbtn;

        for(int i = 0; i < rest.menuTypes.size(); i++){
            menuTypeList.add(rest.menuTypes.get(i).typeName);
        }

        // 스피터 값 할당
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                menuTypeList);
        typeSpiner.setAdapter(adapter);
        typeSpiner.setSelection(0);
    }
}
