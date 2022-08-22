package com.example.nolo.entities.item.purchasable;

import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.variant.ItemVariant;

public interface IPurchasable {
    IItemVariant getItemVariant();
    int getQuantity();
    void addToQuantity(int quantity);
    void incrementOrDecrementQuantity(boolean isIncrement);
}
