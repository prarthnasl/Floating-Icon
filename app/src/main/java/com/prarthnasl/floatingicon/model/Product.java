package com.prarthnasl.floatingicon.model;

/**
 * Created by prarthnasl on 4/2/2016.
 */

public class Product {

    private String name;
    private int image;

    public Product(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}

