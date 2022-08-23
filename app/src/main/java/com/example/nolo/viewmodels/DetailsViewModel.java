package com.example.nolo.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.nolo.entities.item.IItem;
import com.example.nolo.entities.item.colour.Colour;
import com.example.nolo.entities.item.colour.IColour;
import com.example.nolo.entities.item.purchasable.IPurchasable;
import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.entities.item.specs.ISpecs;
import com.example.nolo.entities.item.specs.Specs;
import com.example.nolo.entities.item.specs.specsoption.SpecsOption;
import com.example.nolo.entities.item.storevariants.StoreVariant;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.interactors.item.GetItemByIdUseCase;
import com.example.nolo.interactors.store.GetStoreByIdUseCase;
import com.example.nolo.interactors.user.AddCartItemUseCase;
import com.example.nolo.interactors.user.AddViewedItemUseCase;

import java.util.ArrayList;
import java.util.List;

public class DetailsViewModel extends ViewModel {
    private int quantity;
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
        this.quantity = 1;
        this.purchasable = new Purchasable((ItemVariant) this.itemVariant, this.quantity);
    }

    public List<ItemVariant> getRecItemVariants() {
        List<ItemVariant> ret = new ArrayList<>();
        if (getItemCategory().equals(CategoryType.accessories)) return ret;

        List<String> ids = item.getRecommendedAccessoryIds();
        for (String id : ids) {
            IItem item = GetItemByIdUseCase.getItemById(id);

            if (item != null) {
                ret.add((ItemVariant) item.getDefaultItemVariant());
            }
        }

        return ret;
    }

    public List<String> getImageUrisByColour() {
        Colour colour = getVariantColour();

        boolean found = false;
        List<String> images = new ArrayList<>();
        for (String uri : item.getImageUris()) {
            String[] parts = uri.split("_");
            String col = parts[parts.length - 1];

            if (col.equals(colour.getName())) {
                found = true;
                images.add(uri);
            }
        }

        if (!found) {
            String[] parts = item.getImageUris().get(0).split("_");
            String col = parts[parts.length - 1];
            for (String uri : item.getImageUris()) {
                String[] parts2 = uri.split("_");
                String col2 = parts2[parts2.length - 1];

                if (col2.equals(col)) {
                    found = true;
                    images.add(uri);
                }
            }
        }

        return images;
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

    public CategoryType getItemCategory() {
        return itemVariant.getCategoryType();
    }

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

    public ISpecs getItemSpecs() {
        return item.getSpecs();
    }

    public IPurchasable getPurchasable() {
        return purchasable;
    }

    public void incrementOrDecrementQuantity(boolean isIncrement) {
        if (isIncrement) {
            this.quantity++;
        } else {
            if (this.quantity > 1) {
                this.quantity--;
            }
        }
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void addCart() {
        this.purchasable.addToQuantity(quantity - 1);
        AddCartItemUseCase.addCart(this.purchasable);
    }

    public void addViewHistory() {
        AddViewedItemUseCase.addViewHistory(this.itemVariant);
    }


}
