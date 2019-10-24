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

    //3. 포지션 (요리사들의 업무분담)
    ArrayList<Position> positions;

    public Restaurant(String name, String info){
        this.name = name;
        this.info = info;

        menuTypes = new ArrayList<MenuType>();
        positions = new ArrayList<Position>();
    }

    public ArrayList<MenuType> getMenuTypes() {return menuTypes;}

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

    // 메뉴타입 추가
    public void addMenuType(String name){
        menuTypes.add(new MenuType(name));
    }

    // 포지션 추가
    public void addPosition(String name,int size){
        positions.add(new Position(name,size));
    }
}
