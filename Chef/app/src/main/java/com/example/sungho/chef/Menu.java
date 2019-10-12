package com.example.sungho.chef;

import java.io.Serializable;

/**
 * Created by sungho on 2019-10-04.
 */

public class Menu implements Serializable{
    String name;
    String menuType;
    int price;
    String info;

    public Menu(String name, String menuType, int price, String info){
        this.name = name;
        this.menuType = menuType;
        this.price = price;
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
