package com.example.sungho.chef;

import java.io.Serializable;

/**
 * Created by sungho on 2019-10-13.
 */

public class Cook implements Serializable{
    String name;
    String position;

    public Cook(String name, String position){
        this.name = name;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
