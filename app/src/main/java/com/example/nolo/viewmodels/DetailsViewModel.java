package com.example.nolo.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.nolo.entities.item.IItem;
import com.example.nolo.entities.item.colour.Colour;
import com.example.nolo.entities.item.specs.ISpecs;
import com.example.nolo.entities.item.specs.specsoption.SpecsOption;
import com.example.nolo.entities.item.storevariants.StoreVariant;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.enums.SpecsOptionType;
import com.example.nolo.interactors.item.GetAccessRecommendationsByItemIdUseCase;
import com.example.nolo.interactors.item.GetItemByIdUseCase;
import com.example.nolo.interactors.store.GetStoreByIdUseCase;
import com.example.nolo.interactors.user.AddCartItemUseCase;
import com.example.nolo.interactors.user.AddViewedItemUseCase;
import com.example.nolo.interactors.user.AddWishlistItemUseCase;
import com.example.nolo.interactors.user.GetWishlistItemsUseCase;
import com.example.nolo.interactors.user.RemoveWishlistItemsUseCase;

import java.util.ArrayList;
import java.util.List;

public class DetailsViewModel extends ViewModel implements IDetailsViewModel {
    public static IItemVariant itemVariantFromMap = null;
    private int quantity;
    private IItemVariant itemVariant;
    private IItem item;

    public DetailsViewModel(IItemVariant itemVariant) {
        this.itemVariant = itemVariant;
        this.item = GetItemByIdUseCase.getItemById(itemVariant.getItemId());
        this.quantity = 1;
    }

    private StoreVariant getStoreVariant() {
        String variantStoreId = itemVariant.getStoreId();
        return item.getStoreVariants().stream()
                .filter(o -> o.getStoreId().equals(variantStoreId))
                .findFirst()
                .get();
    }

    @Override
    public List<ItemVariant> getRecItemVariants() {
        List<ItemVariant> ret = new ArrayList<>();
        if (getItemCategory().equals(CategoryType.accessories)) return ret;

        ret = GetAccessRecommendationsByItemIdUseCase.getAccessRecommendationsByItemId(item.getItemId());

        return ret;
    }

    @Override
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

    @Override
    public IItemVariant getItemVariant() {
        return itemVariant;
    }

    @Override
    public void setItemVariant(IItemVariant itemVariant) {
        this.itemVariant = itemVariant;
    }

    @Override
    public String getItemName() {
        return item.getName();
    }

    @Override
    public List<Colour> getItemColours() {
        return getStoreVariant().getColours();
    }

    @Override
    public Colour getVariantColour() {
        return itemVariant.getColour();
    }

    @Override
    public String getVariantColourInString() {
        return capitaliseFirst(itemVariant.getColour().getName());
    }

    @Override
    public String getStoreBranchName() {
        String branchName = itemVariant.getBranchName();
        String storeName = GetStoreByIdUseCase.getStoreById(itemVariant.getStoreId()).getStoreName();

        return storeName + " " + branchName;
    }

    @Override
    public CategoryType getItemCategory() {
        return itemVariant.getCategoryType();
    }

    @Override
    public List<SpecsOption> getStorageOptions() {
        if (getItemCategory() == CategoryType.laptops || getItemCategory() == CategoryType.phones) {
            System.out.println(item.getSpecs().toString());
            return item.getSpecs().getCustomisableSpecs().get(SpecsOptionType.storage.name());
        } else {
            System.err.println("Storage options not available");
            return null;
        }
    }

    @Override
    public List<SpecsOption> getRamOptions() {
        if (getItemCategory() == CategoryType.laptops) {
            return item.getSpecs().getCustomisableSpecs().get(SpecsOptionType.ram.name());
        } else {
            System.err.println("RAM options not available");
            return null;
        }
    }

    @Override
    public ISpecs getItemSpecs() {
        return item.getSpecs();
    }

    @Override
    public void incrementOrDecrementQuantity(boolean isIncrement) {
        if (isIncrement) {
            this.quantity++;
        } else {
            if (this.quantity > 1) {
                this.quantity--;
            }
        }
    }

    @Override
    public int getQuantity() {
        return this.quantity;
    }

    @Override
    public void addCart() {
        AddCartItemUseCase.addCart(this.itemVariant, quantity);
    }

    @Override
    public void addViewHistory() {
        AddViewedItemUseCase.addViewHistory(this.itemVariant);
    }

    @Override
    public boolean isInWishlist() {
        return GetWishlistItemsUseCase.getWishlist().contains(itemVariant);
    }

    @Override
    public void addWishlist() {
        AddWishlistItemUseCase.addWishlist(this.itemVariant);
    }

    @Override
    public void removeWishlist() {
        RemoveWishlistItemsUseCase.removeWishlistItem(this.itemVariant);
    }

    @Override
    public String getItemVariantPriceInString() {
        return itemVariant.getDisplayPrice() + " NZD";
    }

    private String capitaliseFirst(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
