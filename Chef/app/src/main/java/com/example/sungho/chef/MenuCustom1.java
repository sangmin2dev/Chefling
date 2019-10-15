package com.example.sungho.chef;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.sungho.chef.databinding.ActivityMenucustom1Binding;

public class MenuCustom1 extends AppCompatActivity {
    ActivityMenucustom1Binding binding;
    ImageButton nextButton;
    EditText nameEdit;
    EditText infoEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_menucustom1);
        binding.setActivity(this);

        nextButton = binding.nextbtn;
        nameEdit = binding.nameEdit;
        infoEdit = binding.infoEdit;

        nextButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                if(nameEdit.getText().length() == 0)
                    Toast.makeText(getApplicationContext(),"레스토랑 이름을 입력하세요.",Toast.LENGTH_LONG).show();
                else if(infoEdit.getText().length() == 0)
                    Toast.makeText(getApplicationContext(), "레스토랑 정보를 입력하세요.", Toast.LENGTH_SHORT).show();
                else {  //정보 입력 확인시 정보 저장후 다음페이지
                    Restaurant rest = new Restaurant(nameEdit.getText().toString(), infoEdit.getText().toString());
                    Intent intent = new Intent(getApplicationContext(), MenuCustom2.class);
                    intent.putExtra("data",rest);
                    startActivity(intent);
                }
            }
        });
    }
}
