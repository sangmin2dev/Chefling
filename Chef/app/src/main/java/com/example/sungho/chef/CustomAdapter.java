package com.example.sungho.chef;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sungho.chef.Data.Foods;
import com.example.sungho.chef.Data.MenuData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;

class CustomAdapter extends BaseExpandableListAdapter {
    ArrayList<String> groupDatas;
    ArrayList<ArrayList<Foods>> childDatas;
    private LayoutInflater inflater = null;
    MenuData menuData;

    //image
    private File tempFile;
    Uri photoUri;

    public CustomAdapter(MenuData menuData, Context c, ArrayList<String> groupDatas, ArrayList<ArrayList<Foods>> childDatas){
        super();
        this.menuData = menuData;
        this.groupDatas = groupDatas;
        this.childDatas = childDatas;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return groupDatas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childDatas.get(groupPosition).size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return groupDatas.get(groupPosition);
    }

    @Override
    public Foods getChild(int groupPosition, int childPosition) {
        return childDatas.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    // 부모 리스트뷰
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.parent_list_view,null);
        }
        TextView parent_TextView = convertView.findViewById(R.id.parentText);
        parent_TextView.setText(groupDatas.get(groupPosition));

        return convertView;
    }

    // 자식 리스트뷰
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.child_position,null);

            setActivityPosition(groupPosition,childPosition,convertView);
        }
        return convertView;
    }

    // Child를 클릭할일이 있다면 true 로바꾸자
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    // 액티비티 설정
    public void setActivityPosition(int groupPosition, int childPosition, View convertView){
        ImageView menu_Image = (ImageView)convertView.findViewById(R.id.menuImage);
        EditText name_EditText = (EditText)convertView.findViewById(R.id.nameEdit);
        EditText price_EditText = (EditText)convertView.findViewById(R.id.priceEdit);
        EditText time_EditText = (EditText)convertView.findViewById(R.id.timeEdit);
        final EditText info_EditText = (EditText)convertView.findViewById(R.id.infoEdit);

        downloadImage(menu_Image,childDatas.get(groupPosition).get(childPosition).getName(),convertView);

        name_EditText.setText(childDatas.get(groupPosition).get(childPosition).getName());
        price_EditText.setText(childDatas.get(groupPosition).get(childPosition).getPrice()+"");
        time_EditText.setText(childDatas.get(groupPosition).get(childPosition).getCooking_time()+"");
        info_EditText.setText(childDatas.get(groupPosition).get(childPosition).getDescription());

        Log.d("test2",groupPosition+" , "+childPosition);
    }

    // 이미지 불러오기
    public void downloadImage(final ImageView image, String fileName, final View convertView){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://chefling-f122c.appspot.com").child("Menu_pic/" + fileName);
        storageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    GlideApp.with(convertView)
                            .load(task.getResult())
                            .into(image);
                }else{
                }
            }
        });
    }
}
