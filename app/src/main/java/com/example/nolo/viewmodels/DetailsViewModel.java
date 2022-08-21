package com.example.nolo.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.nolo.entities.item.IItem;
import com.example.nolo.entities.item.purchasable.IPurchasable;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.interactors.item.GetItemByIdUseCase;
import com.example.nolo.interactors.user.AddCartItemUseCase;
import com.example.nolo.interactors.user.AddViewedItemUseCase;

public class DetailsViewModel extends ViewModel {
    private IItemVariant itemVariant;
    private IItem item;

    public DetailsViewModel(IItemVariant itemVariant) {
        this.itemVariant = itemVariant;
        this.item = GetItemByIdUseCase.getItemById(itemVariant.getItemId());
    }

    public String getItemName() {
        return item.getName();
    }

    public void addCart(IPurchasable cartItem) {
        AddCartItemUseCase.addCart(cartItem);
    }

    public void addViewHistory(IItemVariant item) {
        AddViewedItemUseCase.addViewHistory(item);
    }


}
