package com.example.sungho.chef;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import androidx.annotation.NonNull;

public class OrderDialog{
    private Context context;

    EditText table;
    LinearLayout list;
    TextView total;
    Button okButton;
    Button cancleButton;
    RadioGroup radioGroup;
    RadioButton together;
    RadioButton separate;

    ArrayList<Foods> cartList;
    ArrayList<Cart> added;
    ArrayList<Foods> notDessertList;
    int id;
    boolean isDessertOrdered = false;

    public OrderDialog(){
        added = new ArrayList<Cart>();
        notDessertList = new ArrayList<Foods>();
    }

    public OrderDialog(Context context){
        this.context = context;
        added = new ArrayList<Cart>();
        notDessertList = new ArrayList<Foods>();
    }

    protected void callFunction(ArrayList<Foods> cartList2) {
        this.cartList = cartList2;
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);
        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.order_dialog);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dlg.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Window window = dlg.getWindow();
        window.setAttributes(lp);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        table = dlg.findViewById(R.id.table);
        list = dlg.findViewById(R.id.list);
        total = dlg.findViewById(R.id.total);
        okButton = dlg.findViewById(R.id.okButton);
        cancleButton = dlg.findViewById(R.id.cancelButton);
        radioGroup = dlg.findViewById(R.id.radioGroup);
        together = dlg.findViewById(R.id.together);
        separate = dlg.findViewById(R.id.separate);
        table.setText("A10");
        cartToAdded();

        // 만약 디저트가 없으면 디저트관련 라디오버튼은 필요없으니 비활성화
        if(!checkDessert()){
            radioGroup.removeAllViews();
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(added.size() <= 0){
                    Toast.makeText(context, "주문목록이 존재하지 않습니다.",Toast.LENGTH_LONG).show();
                }else{
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference menuRef = database.getReference("order");

                    addedToCart();
                    ((MenuActivity)context).setCartList(cartList);

                    final Order order = new Order();
                    order.setTable(table.getText().toString());
                    // 디저트콜 여부
                    if(radioGroup.getCheckedRadioButtonId() == R.id.together){  // 함께 주세요
                        isDessertOrdered = true;
                        order.setFoods(cartList);
                    }else{                                                      // 따로 주세요
                        isDessertOrdered = false;
                        for(int i = 0; i < cartList.size(); i++){
                            if(!cartList.get(i).getType().equals("des")) {
                                notDessertList.add(cartList.get(i));
                            }
                        }
                        order.setFoods(notDessertList);
                    }

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

                            ((MenuActivity)context).setDessertOrdered(isDessertOrdered);

                            dlg.dismiss();
                            Toast.makeText(context,"주문이 되었습니다. 잠시만 기다려주세요.",Toast.LENGTH_LONG).show();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
        });
        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
    }

    public boolean checkDessert(){
        for(int i = 0; i < cartList.size(); i++){
            if(cartList.get(i).getType().equals("des"))
                return true;
        }
        return false;
    }

    private void setId(int n){
        id = n;
    }

    private void setTotal(){
        int totalPrice = 0;
        for(int i = 0; i < added.size(); i ++){
            for(int j = 0; j < added.get(i).getCount(); j++){
                totalPrice += added.get(i).getFood().getPrice();
            }
        }
        total.setText(totalPrice+"");
        total.setTextColor(Color.BLACK);
    }

    // 받은 주문목록을 UI에 맞게 재정렬
    private void cartToAdded(){
        // 1. 메뉴의 중복체크
        for(int i = 0; i < cartList.size(); i ++){
            // 처음 보는 메뉴일때
            if(checkDuplicate(cartList.get(i).getName())){
                Cart cart = new Cart(1,cartList.get(i));
                added.add(cart);
            }
            // 이미 존재하는 메뉴라면 + 1
            else{
                for(int j = 0; j < added.size(); j++){
                    if(added.get(j).getFood().getName().equals(cartList.get(i).getName())){
                        added.get(j).setCount(added.get(j).getCount() + 1);
                    }
                }
            }
        }
        //
        for(int i = 0; i < added.size(); i++){
            addMenu(i,added.get(i).getFood().getName(),added.get(i).getCount(),added.get(i).getFood().getPrice());
        }
        setTotal();
    }

    private void addedToCart(){
        cartList.clear();
        for(int i = 0; i < added.size(); i ++){
            for(int j = 0; j < added.get(i).getCount(); j++){
                // Deep Copy
                Foods foods = new Foods();
                Foods f = added.get(i).getFood();

                foods.setType(f.getType());
                foods.setCategory(f.getCategory());
                foods.setSold_out(f.isSold_out());
                foods.setPrice(f.getPrice());
                foods.setDescription(f.getDescription());
                foods.setCooking_time(f.getCooking_time());
                foods.setName(f.getName());
                foods.setFoodId(f.getFoodId());

                cartList.add(foods);
            }
        }
    }

    // 중복 확인 (true : 중복없음, false : 중복있음)
    public boolean checkDuplicate(String s){
        for(int i = 0; i < added.size(); i++){
            if(added.get(i).getFood().getName().equals(s))
                return false;
        }
        return true;
    }

    private void addMenu(final int index, String name, final int count,int price){
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout menuLayout = new LinearLayout(context);
        menuLayout.setLayoutParams(layoutParams);
        menuLayout.setOrientation(LinearLayout.HORIZONTAL);
        layoutParams.setMargins(20,20,0,0);


        LinearLayout.LayoutParams textLayout =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textLayout.setMargins(20,0,20,0);
        textLayout.gravity = Gravity.CENTER;

        LinearLayout.LayoutParams imageLayout =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageLayout.gravity = Gravity.CENTER;
        imageLayout.width = 120;
        imageLayout.height = 120;

        // 미트볼 스파게티
        TextView nameText = new TextView(context);
        nameText.setText(name);
        nameText.setTypeface(null, Typeface.BOLD);
        nameText.setTextSize(20);
        nameText.setLayoutParams(textLayout);

        // 메뉴 개수
        final TextView countText = new TextView(context);
        countText.setText(count + "");
        countText.setTextSize(30);
        countText.setLayoutParams(textLayout);

        // 메뉴 가격
        final TextView priceText = new TextView(context);
        priceText.setText((price*added.get(index).getCount())+"");
        priceText.setTextSize(20);
        priceText.setLayoutParams(textLayout);

        ImageButton minus = new ImageButton(context);
        minus.setLayoutParams(imageLayout);
        minus.setImageResource(R.drawable.removebtn);
        minus.setBackgroundColor(Color.parseColor("#ffffffff"));
        minus.setScaleType(ImageView.ScaleType.FIT_CENTER);

        ImageButton plus = new ImageButton(context);
        plus.setLayoutParams(imageLayout);
        plus.setImageResource(R.drawable.addbtn);
        plus.setBackgroundColor(Color.parseColor("#ffffffff"));
        plus.setScaleType(ImageView.ScaleType.FIT_CENTER);

        // 갯수조정
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = added.get(index).getCount();
                if(n > 0) {
                    n -= 1;
                    added.get(index).setCount(n);
                    countText.setText(n+"");
                    setTotal();
                    priceText.setText((added.get(index).getFood().getPrice() * n)+"");
                }
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = added.get(index).getCount();
                if(n >= 0) {
                    n += 1;
                    added.get(index).setCount(n);
                    countText.setText(n+"");
                    setTotal();
                    priceText.setText((added.get(index).getFood().getPrice() * n)+"");
                }
            }
        });

        menuLayout.addView(nameText);
        menuLayout.addView(minus);
        menuLayout.addView(countText);
        menuLayout.addView(plus);
        menuLayout.addView(priceText);
        list.addView(menuLayout);
    }
}