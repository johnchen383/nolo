package com.example.nolo.entities.item.purchasable;

import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.enums.PurchaseStatus;

public interface IPurchasable {
    ItemVariant getItemVariant();
    int getQuantity();
    void addToQuantity(int quantity);
    void incrementOrDecrementQuantity(boolean isIncrement);
    PurchaseStatus getStatus();
    void setStatus(PurchaseStatus status);
}
