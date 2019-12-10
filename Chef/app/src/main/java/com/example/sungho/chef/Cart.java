package com.example.sungho.chef;


import com.example.sungho.chef.Data.Foods;

class Cart{
    int count;
    Foods food;

    public Cart(int count, Foods food){
        this.count = count;
        this.food = food;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Foods getFood() {
        return food;
    }

    public void setFood(Foods food) {
        this.food = food;
    }
}