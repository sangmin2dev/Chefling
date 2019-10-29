package com.example.sungho.chef;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sungho.chef.Data.Foods;
import com.example.sungho.chef.databinding.ActivityMenuBinding;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.InputStream;
import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity{
    ActivityMenuBinding binding;
    TextView restName;
    ImageView menuImg;

    ArrayList<Foods> foodList = new ArrayList<Foods>();
    ArrayList<String> menuTypes = new ArrayList<String>();
    String name;

    TabHost tabHost;
    String category = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   // 상태바 삭제
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu);
        binding.setActivity(this);
        restName = binding.restName;
        tabHost = binding.tabHost;
        //tabContent = binding.tabcontent;

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference menuRef = database.getReference("menu");

        menuRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 데이터 받아오기
                parsing(dataSnapshot);

                //1. 입력한 레스토랑 이름 상단에 표시
                restName.setText(name);
                tabHost.setup();
                for(int i = 0; i < menuTypes.size(); i++) {
                    //3. 입력한 메뉴 타입으로 Tab 생성
                    category = menuTypes.get(i);
                    tabHost.addTab(tabHost.newTabSpec(""+i).
                            setContent(new MyTabContentFactory()).
                            setIndicator(menuTypes.get(i)));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void parsing(DataSnapshot dataSnapshot){
        // 중간데모를 위한 임시 파싱
        for( DataSnapshot key : dataSnapshot.getChildren() ) {
            for( DataSnapshot key2 : key.getChildren() ) {              //foods
                if(key2.getKey().equals("foods")) {
                    for (DataSnapshot key3 : key2.getChildren()) {      //1..2..3
                        Foods food= new Foods();
                        for(DataSnapshot data : key3.getChildren()) {   // 속성
                            // 카테고리 입력
                            if(data.getKey().equals("category") && checkDuplicate(data.getValue().toString(),menuTypes)){
                                menuTypes.add(data.getValue().toString());
                            }
                            // 음식정보값 입력
                            switch (data.getKey()){
                                case "category":
                                    food.setCategory(data.getValue().toString());
                                    break;
                                case "cooking_time":
                                    food.setCooking_time(Integer.parseInt(data.getValue().toString()));
                                    break;
                                case "description":
                                    food.setDescription(data.getValue().toString());
                                    break;
                                case "name":
                                    food.setName(data.getValue().toString());
                                    break;
                                case "price":
                                    food.setPrice(Integer.parseInt(data.getValue().toString()));
                                    break;
                                case "sold_out":
                                    food.setSold_out(false);
                                    break;
                            }
                        }
                        foodList.add(food);
                    }
                }else if(key2.getKey().equals("rest_info")){
                    for(DataSnapshot data : key2.getChildren()) {   // 속성
                        if(data.getKey().equals("name"))
                            name = data.getValue().toString();
                    }
                }
            }
        }
    }

    // 중복 확인
    public boolean checkDuplicate(String s,ArrayList<String> list){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).equals(s))
                return false;
        }
        return true;
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
            for(int j = 0; j < foodList.size(); j++) {
                // 카테고리별로 분류
                if(foodList.get(j).getCategory().equals(category)) {
                    // 메뉴 이름
                    TextView menuName = new TextView(MenuActivity.this);
                    menuName.setLayoutParams(layoutParams);
                    menuName.setText(foodList.get(j).getName());
                    menuName.setTextSize(20);
                    menuName.setTextColor(Color.BLACK);
                    menuName.setTypeface(null, Typeface.BOLD);

                    // 메뉴 이미지
                    menuImg = new ImageView(MenuActivity.this);
                    menuImg.setImageResource(R.drawable.loading);
                    menuImg.setLayoutParams(layoutParams);
                    menuImg.setAdjustViewBounds(true);
                    menuImg.setScaleType(ImageView.ScaleType.FIT_CENTER);

                    downloadImage(menuImg,foodList.get(j).getName());

                    //메뉴 가격
                    TextView menuPrice = new TextView(MenuActivity.this);
                    menuPrice.setLayoutParams(layoutParams);
                    menuPrice.setText(foodList.get(j).getPrice() + " 원");
                    menuPrice.setTextSize(15);
                    menuPrice.setTypeface(null, Typeface.ITALIC);

                    //메뉴 설명
                    TextView menuInfo = new TextView(MenuActivity.this);
                    menuInfo.setLayoutParams(layoutParams);
                    menuInfo.setText(foodList.get(j).getDescription());
                    menuInfo.setTextSize(15);

                    // container > Img,Text
                    container.addView(menuImg);
                    container.addView(menuName);
                    container.addView(menuPrice);
                    container.addView(menuInfo);
                }
            }
            scrollView.addView(container);                  // scrollview > container > Img,Text
            tab.addView(scrollView);                        // tab > scrollview > container > Img,Text

            return tab;
        }
    }

    public void downloadImage(final ImageView image, String fileName){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://chefling-f122c.appspot.com").child("Menu_pic/" + fileName);
        storageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    GlideApp.with(MenuActivity.this)
                            .load(task.getResult())
                            .into(image);
                }else{
                    Toast.makeText(MenuActivity.this, "이미지 불러오기를 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}


