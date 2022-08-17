package com.example.nolo.entities.item.variants;

import com.example.nolo.entities.item.colour.IColour;

import java.util.List;

public interface IItemStoreVariant {
    String getStoreId();
    List<IColour> getColours();
    double getBasePrice();
}
