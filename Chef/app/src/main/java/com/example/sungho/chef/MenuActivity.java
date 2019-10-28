package com.example.sungho.chef;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.sungho.chef.Data.Foods;
import com.example.sungho.chef.Data.MenuData;
import com.example.sungho.chef.Data.RestaurantInfo;
import com.example.sungho.chef.databinding.ActivityMenuBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {
    ActivityMenuBinding binding;
    MenuData menuData;
    TextView restName;

    TabHost tabHost;
    FrameLayout tabContent;
    Restaurant rest;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   // 상태바 삭제
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu);
        binding.setActivity(this);
        restName = binding.restName;
        tabHost = binding.tabhost;
        tabContent = binding.tabcontent;

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference menuRef = database.getReference();
        menuRef.child("menu").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                menuData = dataSnapshot.getValue(MenuData.class);
                Log.d("test","menuData : "+menuData.getFoods().get(0).getName());
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        //1. 입력한 레스토랑 이름 상단에 표시
//        restName.setText(rest.getName());

//        tabHost.setup();
//        count = 0;
//        for(int i = 0; i < rest.menuTypes.size(); i++) {
//            //3. 입력한 메뉴 타입으로 Tab 생성
//            tabHost.addTab(tabHost.newTabSpec(""+i).
//                    setContent(new MyTabContentFactory()).
//                    setIndicator(rest.menuTypes.get(i).getTypeName()));
//        }
    }

    class MyTabContentFactory implements TabHost.TabContentFactory{

        @Override
        public View createTabContent(String tag) {
            //2. 각 Tab에 입력한 메뉴들 정보 구성
            // -> 2.1 메뉴타입에 맞는 탭뷰(Tab -> Scroll -> LinearView)를 생성
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            LinearLayout tab = new LinearLayout(MenuActivity.this);
            tab.setLayoutParams(layoutParams);
            tab.setOrientation(LinearLayout.VERTICAL);
            tab.setId(View.generateViewId());   //탭 id

            ScrollView scrollView = new ScrollView(MenuActivity.this);
            scrollView.setLayoutParams(layoutParams);

            LinearLayout container = new LinearLayout(MenuActivity.this);
            container.setOrientation(LinearLayout.VERTICAL);
            container.setLayoutParams(layoutParams);

            // -> 2.2 메뉴타입에 존재하는 메뉴들의 정보를 각자 알맞은 뷰로 생성
            Log.e("에러",count+"");
            ArrayList<Menu> menuList = rest.getMenuTypes().get(count).getMenus();
            count++;
            for(int j = 0; j < menuList.size(); j++) {
                // 메뉴 이미지
                ImageView menuImg = new ImageView(MenuActivity.this);
                menuImg.setImageResource(R.drawable.example1);
                menuImg.setLayoutParams(layoutParams);
                menuImg.setAdjustViewBounds(true);
                menuImg.setScaleType(ImageView.ScaleType.FIT_CENTER);

                // 메뉴 이름
                TextView menuName = new TextView(MenuActivity.this);
                menuName.setLayoutParams(layoutParams);
                menuName.setText(menuList.get(j).getName());
                menuName.setTextSize(20);
                menuName.setTextColor(Color.BLACK);
                menuName.setTypeface(null, Typeface.BOLD);

                //메뉴 가격
                TextView menuPrice = new TextView(MenuActivity.this);
                menuPrice.setLayoutParams(layoutParams);
                menuPrice.setText(menuList.get(j).getPrice());
                menuPrice.setTextSize(15);
                menuPrice.setTypeface(null,Typeface.ITALIC);

                //메뉴 설명
                TextView menuInfo = new TextView(MenuActivity.this);
                menuInfo.setLayoutParams(layoutParams);
                menuInfo.setText(menuList.get(j).getInfo());
                menuInfo.setTextSize(15);

                // container > Img,Text
                container.addView(menuImg);
                container.addView(menuName);
                container.addView(menuPrice);
                container.addView(menuInfo);
            }
            scrollView.addView(container);                  // scrollview > container > Img,Text
            tab.addView(scrollView);                        // tab > scrollview > container > Img,Text

            return tab;
        }

    }
}

