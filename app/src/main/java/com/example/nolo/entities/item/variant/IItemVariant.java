package com.example.nolo.entities.item.variant;

import com.example.nolo.entities.item.colour.Colour;
import com.example.nolo.entities.item.colour.IColour;
import com.example.nolo.entities.item.specs.specsoption.ISpecsOption;
import com.example.nolo.entities.item.specs.specsoption.SpecsOption;
import com.example.nolo.enums.CategoryType;

public interface IItemVariant {
    Colour getColour();
    String getItemId();
    CategoryType getCategoryType();
    String getStoreId();
    String getBranchName();
    SpecsOption getStorageOption();
    SpecsOption getRamOption();
}
