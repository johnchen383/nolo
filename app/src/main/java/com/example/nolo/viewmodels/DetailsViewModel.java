package com.example.nolo.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.nolo.entities.item.IItem;
import com.example.nolo.entities.item.colour.Colour;
import com.example.nolo.entities.item.colour.IColour;
import com.example.nolo.entities.item.purchasable.IPurchasable;
import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.entities.item.specs.specsoption.SpecsOption;
import com.example.nolo.entities.item.storevariants.StoreVariant;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.interactors.item.GetItemByIdUseCase;
import com.example.nolo.interactors.store.GetStoreByIdUseCase;
import com.example.nolo.interactors.user.AddCartItemUseCase;
import com.example.nolo.interactors.user.AddViewedItemUseCase;

import java.util.List;

public class DetailsViewModel extends ViewModel {
    private IItemVariant itemVariant;
    private IItem item;
    private IPurchasable purchasable;

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
        this.purchasable = new Purchasable((ItemVariant) this.itemVariant, 1);
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

    public String getStoreBranchName() {
        String branchName = itemVariant.getBranchName();
        String storeName = GetStoreByIdUseCase.getStoreById(itemVariant.getStoreId()).getStoreName();

        return storeName + " " + branchName;
    }

    public CategoryType getItemCategory() { return itemVariant.getCategoryType(); }

    public List<SpecsOption> getStorageOptions() {
        if (getItemCategory() == CategoryType.laptops || getItemCategory() == CategoryType.phones) {
            System.out.println(item.getSpecs().toString());
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

    public IPurchasable getPurchasable() {
        return purchasable;
    }

    public void addCart() {
        AddCartItemUseCase.addCart(this.purchasable);
    }

    public void addViewHistory() {
        AddViewedItemUseCase.addViewHistory(this.itemVariant);
    }


}
