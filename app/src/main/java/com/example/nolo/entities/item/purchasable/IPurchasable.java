package com.example.nolo.entities.item.purchasable;

import com.example.nolo.entities.item.variant.IItemVariant;

public interface IPurchasable {
    IItemVariant getItemVariant();
    Integer getQuantity();
    void incrementQuantity(Integer increment);
}
