package com.example.sungho.chef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sungho.chef.Data.Foods;
import com.example.sungho.chef.Data.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MenuDialog extends AppCompatActivity {
    private Context context;

    LinearLayout menulayout;
    LinearLayout dessertlayout;
    LinearLayout descall;
    TextView dessertText;
    Button dessertCall;
    Button okButton;
    Button cancleButton;

    ArrayList<Foods> cartList;
    ArrayList<Foods> dessertList;
    ArrayList<Time> timeList;

    boolean isDessertOrdered = false;
    int id = 0;
    public MenuDialog(){
        this.dessertList = new ArrayList<Foods>();
    }

    public MenuDialog(Context context){
        this.context = context;
        this.dessertList = new ArrayList<Foods>();
    }

    // 0
    public void callFunction(ArrayList<Foods> cartList, final boolean isDessertOrdered){
        this.cartList = cartList;
        timeList = new ArrayList<Time>();
        this.isDessertOrdered = isDessertOrdered;

        final Dialog dlg = new Dialog(context);
        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.menu_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dlg.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Window window = dlg.getWindow();
        window.setAttributes(lp);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        menulayout = dlg.findViewById(R.id.menulayout);
        dessertlayout = dlg.findViewById(R.id.dessertlayout);
        dessertText = dlg.findViewById(R.id.dessertText);
        dessertCall = dlg.findViewById(R.id.dessertCall);
        descall = dlg.findViewById(R.id.descall);
        okButton = dlg.findViewById(R.id.okButton);
        cancleButton = dlg.findViewById(R.id.cancelButton);

        // 모든 디저트를 주문한 상태라면 디저트콜 버튼 삭제
        if(isDessertOrdered){
            descall.removeAllViews();
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference processingRef = database.getReference("processing");

        processingRef.child("/5").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                parsingTime(dataSnapshot);  // 1. 파이어베이스에서 대기 요리갯수, 대기시간 정보를 전부 받아온다.
                logTime();
                addMenu();                  // 2. 받아온 정보와 주문한 정보에 대기 요리갯수, 대기시간을 레이아웃으로 표현한다.
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        dessertCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isDessertOrdered && checkDessert()){
                    setDessertOrdered(true);
                    ((MenuActivity)context).setDessertOrdered(true);

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference menuRef = database.getReference("order");

                    final Order order = new Order();
                    order.setFoods(dessertList);
                    order.setTable("A10");      // 수정필요

                    final DatabaseReference menuRef2 = database.getReference("orderID");

                    // orderID 불러오기
                    menuRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            setId(Integer.parseInt(dataSnapshot.getValue().toString()));
                            order.setOrderId(id+"");

                            // orderID 하나 증가
                            menuRef2.setValue((id+1));

                            // Food ID생성
                            for(int i = 0; i < order.getFoods().size(); i++){
                                order.getFoods().get(i).setFoodId(order.getOrderId()+"_"+i);
                            }
                            menuRef.push().setValue(order);

                            Toast.makeText(context,"디저트를 주문했습니다. 잠시만 기다려주세요", Toast.LENGTH_SHORT).show();
                            dlg.dismiss();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
        });
    }

    private void setId(int n){
        id = n;
    }

    public void setDessertOrdered(boolean b){
        isDessertOrdered = b;
    }

    // 1
    public void parsingTime(DataSnapshot dataSnapshot){
        String time_str = "";
        for (DataSnapshot time : dataSnapshot.getChildren()) {      // food 0,1,2...
            time_str = time.getKey();
            Time menuTime = new Time();
            for(DataSnapshot key : time.getChildren()){
                switch (key.getKey()){
                    case "1":   // food_id
                        menuTime.setFoodId(key.getValue().toString());
                        break;
                    case"2":    // Count
                        menuTime.setCount(Integer.parseInt(key.getValue().toString()));
                        break;
                    case "3":   // Time
                        menuTime.setTime(Integer.parseInt(key.getValue().toString()));
                        break;
                }
            }
            timeList.add(menuTime);
        }
    }
    // 디저트 있니?
    public boolean checkDessert(){
        for(int i = 0; i < cartList.size(); i++){
            if(cartList.get(i).getType().equals("des"))
                return true;
        }
        return false;
    }

    // Time 모두 출력
    public void logTime(){
        for(int i = 0 ; i < timeList.size(); i++){
            Time time = timeList.get(i);
            Log.d("시간 확인", time.getFoodId() + " : (갯수 "+time.getCount() + "), (시간 "+time.getTime()+")");
        }
    }

    private void addMenu() {
        for(int i = 0; i < this.cartList.size(); i++){
            Foods foods = cartList.get(i);

            String name = foods.getName();
            String foodId = foods.getFoodId();
            String type = foods.getType();

            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(30, 30, 0, 30);

            LinearLayout menuLayout = new LinearLayout(context);
            menuLayout.setLayoutParams(layoutParams);
            menuLayout.setOrientation(LinearLayout.HORIZONTAL);
            menuLayout.setGravity(Gravity.CENTER_HORIZONTAL);

            LinearLayout.LayoutParams textLayout =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textLayout.gravity = Gravity.CENTER;

            LinearLayout.LayoutParams imageLayout =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            imageLayout.gravity = Gravity.CENTER;
            imageLayout.width = 80;
            imageLayout.height = 80;
            imageLayout.setMargins(30,0,30,0);

            // [2_0] 미트볼 스파게티 []
            TextView nameText = new TextView(context);
            nameText.setText("["+foodId+"] "+name);
            nameText.setTextSize(20);
            nameText.setLayoutParams(textLayout);

            // 시간 아이콘
            ImageView timeImage = new ImageView(context);
            timeImage.setLayoutParams(imageLayout);
            timeImage.setImageResource(R.drawable.timeicon);
            timeImage.setBackgroundColor(Color.parseColor("#ffffffff"));
            timeImage.setScaleType(ImageView.ScaleType.FIT_CENTER);

            menuLayout.addView(nameText);
            menuLayout.addView(timeImage);

            TextView timeText = new TextView(context);
            TextView countText = new TextView(context);

            if(type.equals("des")){
                dessertText.setText("디저트");
                dessertList.add(foods);

                // 디저트를 장바구니에 추가했고 주문이 된 상태.
                if(isDessertOrdered){
                    float time = checkTime(foodId) / 10;
                    int count = checkCount(foodId);

                    String timeStr = time+" 분";
                    String countStr = count+" 개";

                    // 12분
                    timeText.setText(timeStr);
                    timeText.setTextSize(20);
                    timeText.setLayoutParams(textLayout);

                    countText.setText(countStr);
                    countText.setTypeface(null,Typeface.ITALIC);
                    countText.setTextSize(16);
                    countText.setTextColor(Color.parseColor("#F5921D"));
                    countText.setLayoutParams(textLayout);
                }   // 디저트를 장바구니에 추가했지만 디저트는 아직 주문하지 않은상태.
                else {
                    timeText.setText(" 대기중 ");
                    timeText.setTextSize(20);
                    timeText.setLayoutParams(textLayout);

                    countText.setText(" 디저트콜 필요 ");
                    countText.setTypeface(null,Typeface.ITALIC);
                    countText.setTextSize(16);
                    countText.setTextColor(Color.parseColor("#F5921D"));
                    countText.setLayoutParams(textLayout);
                }
                menuLayout.addView(timeText);
                menuLayout.addView(countText);
                dessertlayout.addView(menuLayout);
            }else{
                float time = checkTime(foodId) / 10;
                int count = checkCount(foodId);

                String timeStr = time + "분  ";
                if(time == 0){
                    timeStr = "요리중  ";
                }
                String countStr = count+" 개";

                // 12분
                timeText.setText(timeStr);
                timeText.setTextSize(20);
                timeText.setLayoutParams(textLayout);

                countText.setText(countStr);
                countText.setTypeface(null,Typeface.ITALIC);
                countText.setTextSize(16);
                countText.setTextColor(Color.parseColor("#F5921D"));
                countText.setLayoutParams(textLayout);

                menuLayout.addView(timeText);
                menuLayout.addView(countText);
                menulayout.addView(menuLayout);
            }
        }
    }

    // 해당 foodId의 시간 반환
    public int checkTime(String foodId){
        for(int i = 0; i < timeList.size(); i++){
            if(timeList.get(i).getFoodId().equals(foodId))
                return timeList.get(i).getTime();
        }
        return 0;
    }

    // 해당 foodCount의 시간 반환
    public int checkCount(String foodId){
        for(int i = 0; i < timeList.size(); i++){
            if(timeList.get(i).getFoodId().equals(foodId))
                return timeList.get(i).getCount();
        }
        return 0;
    }
}