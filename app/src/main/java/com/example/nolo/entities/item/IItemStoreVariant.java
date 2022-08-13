package com.example.nolo.entities.item;

import com.example.nolo.entities.item.specs.IColour;

import java.util.List;

public interface IItemStoreVariant {
    String getStoreId();
    List<IColour> getColours();
    double getBasePrice();
}
