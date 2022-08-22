package com.example.nolo.entities.item.variant;

import com.example.nolo.entities.item.colour.Colour;
import com.example.nolo.entities.item.specs.specsoption.SpecsOption;
import com.example.nolo.enums.CategoryType;

public interface IItemVariant {
    Colour getColour();
    void setColour(Colour colour);
    String getItemId();
    CategoryType getCategoryType();
    String getStoreId();
    String getBranchName();
    SpecsOption getStorageOption();
    SpecsOption getRamOption();
    String getTitle();
    String getDisplayPrice();
    String getDisplayImage();
    void setStoreId(String id);
    void setBranchName(String name);
}
