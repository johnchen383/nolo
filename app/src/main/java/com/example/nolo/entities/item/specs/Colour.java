package com.example.nolo.entities.item.specs;

public class Colour implements IColour {
    private String name, hexCode;

    public Colour() {}

    public Colour(String name, String hexCode) {
        this.name = name;
        this.hexCode = hexCode;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getHexCode() {
        return hexCode;
    }
}
