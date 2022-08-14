package com.example.nolo.entities.item;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.GeoPoint;

public class Colour implements IColour{
    private String name;
    private String hexCode;

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
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

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
