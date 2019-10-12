package com.example.sungho.chef;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by sungho on 2019-10-04.
 */

<<<<<<< HEAD
public class MenuType implements Serializable{
=======
public class MenuType implements Serializable {
>>>>>>> 539a3fedcee6cd901a117b5cd751883b04af1702
    String typeName;
    ArrayList<Menu> menus;

    public MenuType(String typeName){
        this.typeName = typeName;
        menus = new ArrayList<Menu>();
    }
<<<<<<< HEAD

    public void AddMenu(String name, String menuType, int price, String info){
        menus.add(new Menu(name, menuType, price, info));
    }

}
=======
}
>>>>>>> 539a3fedcee6cd901a117b5cd751883b04af1702
