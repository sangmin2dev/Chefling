package com.example.sungho.chef;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

class CustomAdapter extends BaseExpandableListAdapter {
    ArrayList<GroupData> groupDatas;
    ArrayList<ArrayList<ChildData>> childDatas;
    private LayoutInflater inflater = null;

    public CustomAdapter(Context c, ArrayList<GroupData> groupDatas, ArrayList<ArrayList<ChildData>> childDatas){
        super();
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
    public GroupData getGroup(int groupPosition) {
        return groupDatas.get(groupPosition);
    }

    @Override
    public ChildData getChild(int groupPosition, int childPosition) {
        return childDatas.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
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
        parent_TextView.setText(groupDatas.get(groupPosition).getGroupNumber()+"");

        return convertView;
    }

    // 자식 리스트뷰
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.child_list_view,null);
        }
        convertView = inflater.inflate(R.layout.child_list_view,null);

        TextView name_TextView = (TextView)convertView.findViewById(R.id.name_text_view);
        TextView tel_TextView = (TextView)convertView.findViewById(R.id.tel_text_view);
        TextView email_TextView = (TextView)convertView.findViewById(R.id.email_text_view);

        name_TextView.setText(childDatas.get(groupPosition).get(childPosition).getName());
        tel_TextView.setText(childDatas.get(groupPosition).get(childPosition).getTel());
        email_TextView.setText(childDatas.get(groupPosition).get(childPosition).getEmail());
        Log.d("group",groupDatas.get(groupPosition).getGroupNumber()+"");

        Log.d("group",childDatas.get(groupPosition).get(childPosition).getName());

        return convertView;
    }

    // Child를 클릭할일이 있다면 true 로바꾸자
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class ChildData{
        private String name;
        private String tel;
        private String email;

        public ChildData(String name, String tel, String email){
            this.name = name;
            this.tel = tel;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public String getTel() {
            return tel;
        }

        public String getEmail() {
            return email;
        }
    }

    static class GroupData{
        private int groupNumber;

        public GroupData(int i){
            this.groupNumber = i;
        }
        public int getGroupNumber(){
            return groupNumber;
        }
    }
}