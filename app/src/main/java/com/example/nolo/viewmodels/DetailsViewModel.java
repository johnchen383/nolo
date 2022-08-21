package com.example.nolo.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.nolo.entities.item.IItem;
import com.example.nolo.entities.item.colour.Colour;
import com.example.nolo.entities.item.purchasable.IPurchasable;
import com.example.nolo.entities.item.storevariants.StoreVariant;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.interactors.item.GetItemByIdUseCase;
import com.example.nolo.interactors.user.AddCartItemUseCase;
import com.example.nolo.interactors.user.AddViewedItemUseCase;

import java.util.List;

public class DetailsViewModel extends ViewModel {
    private IItemVariant itemVariant;
    private IItem item;

    private StoreVariant getStoreVariant() {
        String variantStoreId = itemVariant.getStoreId();
        return item.getStoreVariants().stream().filter(o -> o.getStoreId().equals(variantStoreId)).findFirst().get();
    }

    public DetailsViewModel(IItemVariant itemVariant) {
        this.itemVariant = itemVariant;
        this.item = GetItemByIdUseCase.getItemById(itemVariant.getItemId());
    }

    public String getItemName() {
        return item.getName();
    }


    public List<Colour> getColours() {
        return getStoreVariant().getColours();
    }

    public void addCart(IPurchasable cartItem) {
        AddCartItemUseCase.addCart(cartItem);
    }

    public void addViewHistory(IItemVariant item) {
        AddViewedItemUseCase.addViewHistory(item);
    }


}
