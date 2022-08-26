package com.example.nolo.entities.item.purchasable;

import com.example.nolo.entities.item.variant.ItemVariant;

public interface IPurchasable {
    ItemVariant getItemVariant();
    int getQuantity();
    void addToQuantity(int quantity);
    void incrementOrDecrementQuantity(boolean isIncrement);
    IPurchasable copy();
}
