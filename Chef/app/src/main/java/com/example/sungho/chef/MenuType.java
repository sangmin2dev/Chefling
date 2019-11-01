package com.example.sungho.chef;

import android.net.Uri;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by sungho on 2019-10-04.
 */

public class MenuType implements Serializable{
    String typeName;
    ArrayList<Menu> menus;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public ArrayList<Menu> getMenus() {
        return menus;
    }

    public MenuType(String typeName){
        this.typeName = typeName;
        menus = new ArrayList<Menu>();
    }

    public void AddMenu(String name, String menuType, int price, int time, String info, String uri){
        menus.add(new Menu(name, menuType, price, time, info, uri));
    }
}
