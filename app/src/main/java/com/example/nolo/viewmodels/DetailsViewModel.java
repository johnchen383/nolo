package com.example.nolo.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.nolo.entities.item.IItem;
import com.example.nolo.entities.item.colour.Colour;
import com.example.nolo.entities.item.colour.IColour;
import com.example.nolo.entities.item.purchasable.IPurchasable;
import com.example.nolo.entities.item.specs.specsoption.SpecsOption;
import com.example.nolo.entities.item.storevariants.StoreVariant;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.interactors.item.GetItemByIdUseCase;
import com.example.nolo.interactors.user.AddCartItemUseCase;
import com.example.nolo.interactors.user.AddViewedItemUseCase;

import java.util.List;

public class DetailsViewModel extends ViewModel {
    private IItemVariant itemVariant;
    private IItem item;

    private StoreVariant getStoreVariant() {
        String variantStoreId = itemVariant.getStoreId();
        return item.getStoreVariants().stream()
            .filter(o -> o.getStoreId().equals(variantStoreId))
            .findFirst()
            .get();
    }

    public DetailsViewModel(IItemVariant itemVariant) {
        this.itemVariant = itemVariant;
        this.item = GetItemByIdUseCase.getItemById(itemVariant.getItemId());
    }

    public IItemVariant getItemVariant() {
        return itemVariant;
    }

    public void setItemVariant(IItemVariant itemVariant) {
        this.itemVariant = itemVariant;
    }

    public String getItemName() {
        return item.getName();
    }

    public List<Colour> getItemColours() {
        return getStoreVariant().getColours();
    }

    public Colour getVariantColour() {
        return itemVariant.getColour();
    }

    public CategoryType getItemCategory() { return itemVariant.getCategoryType(); }

    public List<SpecsOption> getStorageOptions() {
        System.out.println(itemVariant.toString());
        if (getItemCategory() == CategoryType.laptops || getItemCategory() == CategoryType.phones) {
            System.out.println("hi" + item.getSpecs().getStorageOptions());
            return item.getSpecs().getStorageOptions();
        } else {
            System.err.println("Storage options not available");
            return null;
        }
    }

    public List<SpecsOption> getRamOptions() {
        if (getItemCategory() == CategoryType.laptops) {
            return item.getSpecs().getRamOptions();
        } else {
            System.err.println("RAM options not available");
            return null;
        }
    }

    public void addCart(IPurchasable cartItem) {
        AddCartItemUseCase.addCart(cartItem);
    }

    public void addViewHistory(IItemVariant item) {
        AddViewedItemUseCase.addViewHistory(item);
    }


}
