package com.example.sungho.chef;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.sungho.chef.databinding.ActivityCookModify1Binding;

import java.util.ArrayList;
import java.util.HashMap;

public class CookModify1 extends AppCompatActivity {
    ActivityCookModify1Binding binding;

    ExpandableListView test_ExpandableListView;
    CustomAdapter adapter;
    ArrayList<CustomAdapter.GroupData> groupListDatas;
    ArrayList<ArrayList<CustomAdapter.ChildData>> childListDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cook_modify1);

        test_ExpandableListView = (ExpandableListView)findViewById(R.id.expandablelist);
        setData();
        adapter = new CustomAdapter(this,groupListDatas,childListDatas);
        test_ExpandableListView.setAdapter(adapter);

        test_ExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });
    }
    private void setData(){
        groupListDatas = new ArrayList<CustomAdapter.GroupData>();
        childListDatas = new ArrayList<ArrayList<CustomAdapter.ChildData>>();
        int sizeList = 0;
        groupListDatas.add(new CustomAdapter.GroupData(0));
        childListDatas.add(new ArrayList<CustomAdapter.ChildData>());
        childListDatas.get(sizeList).add(new CustomAdapter.ChildData(
                "Park","01087039376","sungho0830@naver.com"));
        childListDatas.get(sizeList).add(new CustomAdapter.ChildData(
                "Park","01087039376","sungho0830@naver.com"));

        groupListDatas.add(new CustomAdapter.GroupData(1));
        childListDatas.add(new ArrayList<CustomAdapter.ChildData>());
        sizeList++;
        childListDatas.get(sizeList).add(new CustomAdapter.ChildData(
                "Park","01087039376","sungho0830@naver.com"));
        childListDatas.get(sizeList).add(new CustomAdapter.ChildData(
                "Park","01087039376","sungho0830@naver.com"));

        groupListDatas.add(new CustomAdapter.GroupData(2));
        childListDatas.add(new ArrayList<CustomAdapter.ChildData>());
        sizeList++;
        childListDatas.get(sizeList).add(new CustomAdapter.ChildData(
                "Park","01087039376","sungho0830@naver.com"));
        childListDatas.get(sizeList).add(new CustomAdapter.ChildData(
                "Park","01087039376","sungho0830@naver.com"));
    }

}