package com.example.sungho.chef;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by sungho on 2019-10-04.
 */

public class Menu implements Serializable{
    String name;
    String menuType;
    int price;
    int time;
    String info;
    String uri;

    public Menu(String name, String menuType, int price, int time, String info, String uri){
        this.name = name;
        this.menuType = menuType;
        this.price = price;
        this.time = time;
        this.info = info;
        this.uri = uri;
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

    public int getTime() {return time;}

    public void setTime(int time) {this.time = time;}

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUri() { return uri; }

    public void setUri(String uri) { this.uri = uri; }
}
