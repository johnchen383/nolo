package com.example.nolo.entities.item.colour;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

public class Colour implements IColour, Serializable {
    private String name, hexCode;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Colour colour = (Colour) o;
        return Objects.equals(name, colour.name) && Objects.equals(hexCode, colour.hexCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, hexCode);
    }
}
