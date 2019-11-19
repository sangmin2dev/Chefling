package com.example.sungho.chef.Data;

import java.util.ArrayList;

public class Order {
    String table;
    ArrayList<Foods> foods;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public ArrayList<Foods> getFoods() {
        return foods;
    }

    public void setFoods(ArrayList<Foods> foods) {
        this.foods = foods;
    }
}
