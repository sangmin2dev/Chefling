package com.example.sungho.chef;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sungho.chef.Data.Foods;
import com.example.sungho.chef.Data.MenuData;
import com.example.sungho.chef.databinding.ActivityMenuBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity{
    ActivityMenuBinding binding;
    TextView restName;
    ImageView menuImg;

    boolean isFabOpen = false;
    Animation fab_open, fab_close;
    FloatingActionButton mainButton;
    FloatingActionButton orderButton;
    FloatingActionButton timeButton;

    ArrayList<Foods> foodList = new ArrayList<Foods>();
    ArrayList<String> menuTypes = new ArrayList<String>();
    ArrayList<Foods> cartList = new ArrayList<Foods>();

    String name;

    TabHost tabHost;
    String category = "";
    int length = 0;

    boolean isOrdered = false;
    boolean isDesOrdered = false;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   // 상태바 삭제
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu);
        binding.setActivity(this);
        restName = binding.restName;
        tabHost = binding.tabHost;

        mainButton = binding.main;
        orderButton = binding.order;
        timeButton = binding.time;

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference menuRef = database.getReference("menu");
        menuRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    tabHost.addTab(tabHost.newTabSpec(menuTypes.get(i)).
                            setContent(new MyTabContentFactory()).
                            setIndicator(menuTypes.get(i)));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MenuActivity.this, "메뉴정보 불러오기를 실패하였습니다. 다시 실행해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        // 버튼 열기
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFab();
            }
        });

        // 주문하기 버튼
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cartList.size() != 0){
                    toggleFab();
                    showCart();
                }else{
                    Toast.makeText(MenuActivity.this,"장바구니가 비어있습니다. 메뉴 사진을 눌러서 주문을 먼저 해주세요.",Toast.LENGTH_LONG).show();;
                }
            }
        });

        // 주문목록 보기 버튼(대기시간, 디저트 콜)
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOrdered){
                    toggleFab();
                    showTime();
                }else{
                    Toast.makeText(MenuActivity.this,"아직 주문이 되지 않았습니다. 장바구니 버튼을 눌러서 먼저 주문을 해주세요.",Toast.LENGTH_LONG).show();;
                }

            }
        });
    }

    private void toggleFab() {
        if (isFabOpen) {
            mainButton.setImageResource(R.drawable.plus);
            orderButton.startAnimation(fab_close);
            timeButton.startAnimation(fab_close);
            orderButton.setClickable(false);
            timeButton.setClickable(false);
            isFabOpen = false;
        } else {
            mainButton.setImageResource(R.drawable.minus);
            orderButton.startAnimation(fab_open);
            timeButton.startAnimation(fab_open);
            orderButton.setClickable(true);
            timeButton.setClickable(true);
            isFabOpen = true;
        }
    }

    // FireBase >> Android
    public void parsing(DataSnapshot dataSnapshot){
        for( DataSnapshot key2 : dataSnapshot.getChildren() ) {              //foods
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
                            case "type":
                                food.setType(data.getValue().toString());
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
                            case "complete":
                                food.setComplete(false);
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
                Log.d("dddd",tag+"에 있는 "+foodList.get(j).getName());
                if(foodList.get(j).getCategory().equals(tag)) {
                    Log.d("dddd",category+"에 있는 "+foodList.get(j).getName());
                    // 메뉴 이름
                    final TextView menuName = new TextView(MenuActivity.this);
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
                    final int finalJ = j;
                    menuImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!foodList.get(finalJ).isSold_out())
                                addCart(menuName.getText().toString());
                            else
                                Toast.makeText(MenuActivity.this, "본 메뉴는 매진되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    if (!foodList.get(finalJ).isSold_out())
                        downloadImage(menuImg,foodList.get(j).getName());
                    else
                        menuImg.setImageResource(R.drawable.soldout);

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
    // 이미지 불러오기
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
                    Toast.makeText(MenuActivity.this, "이미지 불러오기를 실패하였습니다. 다시 실행해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 클릭한 메뉴를 카트에 추가
    public void addCart(final String name){
        final NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("주문하기");
        builder.setMessage(name+"를 주문목록에 추가 할까요?");
        builder.setView(numberPicker);
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i = 0; i < foodList.size(); i++){
                            if(foodList.get(i).getName().equals(name)){
                                for(int j = 0; j < numberPicker.getValue(); j ++){
                                    cartList.add(foodList.get(i));
                                }
                                Toast.makeText(MenuActivity.this, foodList.get(i).getName() + numberPicker.getValue()
                                        +"개 가 주문 목록에 담겼습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }

    // 주문을 완료했을때 실행
    public void setCartList(ArrayList<Foods> cartList){
        this.cartList = cartList;
        isOrdered = true;
    }

    // 디저트를 주문했을때 실행
    public void setDessertOrdered(boolean isDessertOrdered){
        this.isDesOrdered = isDessertOrdered;
        Log.d("뭐냐?",this.isDesOrdered + "dd");
    }

    // 카트에 담긴 메뉴목록 보여주기
    public void showCart(){
        final EditText editText = new EditText(this);
        editText.setText("A10");

        //order ID 받기
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference menuRef = database.getReference("order");
        getCurrentId(menuRef);

        // custom dialog
        OrderDialog dialog = new OrderDialog(this);
        dialog.callFunction(cartList);
    }

    // 주문한 목록 보여주기(시간, 디저트콜)
    public void showTime(){
        MenuDialog dialog = new MenuDialog(this);
        dialog.callFunction(cartList,isDesOrdered);
        Log.d("확인", cartList.size()+"개");
    }

    public void getCurrentId(DatabaseReference menuRef){
        menuRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                length = (int)dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}