package com.example.nolo.entities.item;

public interface IPurchasable {
    IItemVariant getItemVariant();
    Integer getQuantity();
    void incrementQuantity(Integer increment);
}
