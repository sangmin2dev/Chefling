package com.example.sungho.chef;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sungho on 2019-10-13.
 */

public class Position implements Serializable{
    String name;
    int size;
    ArrayList<String> typeList;     //해당 포지션에서 담당하는 메뉴타입
    ArrayList<Cook> cooks;

    public Position(String name,int size){
        this.name = name;
        this.size = size;
        typeList = new ArrayList<String>();
        cooks = new ArrayList<Cook>();
    }

    public ArrayList<Cook> getCooks() {
        return cooks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void addType(String type){
        typeList.add(type);
    }
    public void addCook(String cookName) {cooks.add(new Cook(cookName, name));}
}
