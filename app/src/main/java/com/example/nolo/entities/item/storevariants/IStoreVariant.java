package com.example.nolo.entities.item.storevariants;

import com.example.nolo.entities.item.colour.Colour;

import java.util.List;

public interface IStoreVariant {
    String getStoreId();
    List<Colour> getColours();
    double getBasePrice();
}
