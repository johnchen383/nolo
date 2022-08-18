package com.example.nolo.entities.item.variant;

import com.example.nolo.entities.item.colour.IColour;
import com.example.nolo.entities.item.specs.specsoption.ISpecsOption;
import com.example.nolo.enums.CategoryType;

public interface IItemVariant {
    IColour getColour();
    String getItemId();
    CategoryType getCategoryType();
    String getStoreId();
    String getBranchName();
    ISpecsOption getStorageOption();
    ISpecsOption getRamOption();
}
