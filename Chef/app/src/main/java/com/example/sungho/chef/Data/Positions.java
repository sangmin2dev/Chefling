package com.example.sungho.chef.Data;

import com.example.sungho.chef.Cook;

import java.util.ArrayList;

public class Positions {
    String name;
    int size;
    ArrayList<String> typeList;     //해당 포지션에서 담당하는 메뉴타입

    public Positions(){
        typeList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setTypeList(ArrayList<String> typeList){this.typeList = typeList;}
    public int getSize() { return size; }
    public void setSize(int size) {
        this.size = size;
    }
    public void addType(String type){
        typeList.add(type);
    }
}
