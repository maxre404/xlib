package com.ok.uiframe.data;

public class Gift {
    private String name;
    private int imageResourceId;

    public Gift(String name, int imageResourceId) {
        this.name = name;
        this.imageResourceId = imageResourceId;
    }

    public String getName() {
        return name;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}
