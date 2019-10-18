package com.example.sungho.chef.Data;

public class Foods {
    private String name;                //menu , scheduling
    private String category;            //menu , scheduling
    private String description;         //menu
    private int price;                  //menu
    private int cooking_time;           //scheduling
    private boolean sold_out;           //menu , scheduling

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCooking_time() {
        return cooking_time;
    }

    public void setCooking_time(int cooking_time) {
        this.cooking_time = cooking_time;
    }

    public boolean isSold_out() {
        return sold_out;
    }

    public void setSold_out(boolean sold_out) {
        this.sold_out = sold_out;
    }
}
