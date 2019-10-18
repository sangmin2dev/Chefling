package com.example.sungho.chef.Data;

public class Cooks {
    private String name;
    private String position;
    private int ability;
    private boolean breaktime;

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public int getAbility() {
        return ability;
    }

    public boolean getBreaktime() {
        return breaktime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setAbility(int ability) {
        this.ability = ability;
    }

    public void setBreaktime(boolean breaktime) {
        this.breaktime = breaktime;
    }
}

