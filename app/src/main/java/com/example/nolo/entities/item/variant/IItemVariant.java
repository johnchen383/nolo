package com.example.nolo.entities.item.variant;

import com.example.nolo.entities.item.colour.IColour;

public interface IItemVariant {
    IColour getColour();
    String getItemId();
    String getCategoryId();
    String getStoreId();
    String getBranchName();
    String getStorageSize();
    String getRamSize();
}
