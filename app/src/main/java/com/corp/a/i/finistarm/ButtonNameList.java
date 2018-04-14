package com.corp.a.i.finistarm;

/**
 * Created by GordeevMaxim on 14.04.2018.
 */

public class ButtonNameList {
    private String name;
    private int id;

    public ButtonNameList(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public int getPrice() {
        return id;
    }

    public void setPrice(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
