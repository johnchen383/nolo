package com.example.nolo.viewmodels;

import com.example.nolo.entities.item.colour.Colour;
import com.example.nolo.entities.item.specs.ISpecs;
import com.example.nolo.entities.item.specs.specsoption.SpecsOption;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.enums.CategoryType;

import java.util.List;

public interface IDetailsViewModel {
    List<ItemVariant> getRecItemVariants();
    List<String> getImageUrisByColour();
    IItemVariant getItemVariant();
    void setItemVariant(IItemVariant itemVariant);
    String getItemName();
    List<Colour> getItemColours();
    Colour getVariantColour();
    String getVariantColourInString();
    String getStoreBranchName();
    CategoryType getItemCategory();
    List<SpecsOption> getStorageOptions();
    List<SpecsOption> getRamOptions();
    ISpecs getItemSpecs();
    void incrementOrDecrementQuantity(boolean isIncrement);
    int getQuantity();
    void addCart();
    void addViewHistory();
    boolean isInWishlist();
    void addWishlist();
    void removeWishlist();
    String getItemVariantPriceInString();
}
