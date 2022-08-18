package com.example.nolo.entities.item.purchasable;

import com.example.nolo.entities.item.variant.IItemVariant;

public interface IPurchasable {
    IItemVariant getItemVariant();
    int getQuantity();
    void incrementQuantity(int increment);
}
