package com.example.sungho.chef;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sungho on 2019-10-04.
 */

public class Restaurant implements Serializable{
    //1. 레스토랑 정보
    String name = "";   // 레스토랑 이름
    String info = "";   // 레스토랑 설명

    //2. 메뉴 타입
    ArrayList<MenuType> menuTypes;  // 메뉴 타입들




    public Restaurant(String name, String info){
        this.name = name;
        this.info = info;

        menuTypes = new ArrayList<MenuType>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void addMenuType(String name){
        menuTypes.add(new MenuType(name));
    }
}
