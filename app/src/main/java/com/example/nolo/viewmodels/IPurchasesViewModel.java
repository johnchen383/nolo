package com.example.nolo.viewmodels;

import com.example.nolo.entities.item.purchasable.Purchasable;

import java.util.List;

public interface IPurchasesViewModel {
    List<Purchasable> getUserPurchaseHistoryInTransit();
    List<Purchasable> getUserPurchaseHistoryDelivered();
    boolean checkPurchaseHistoryInTransitEmpty();
    boolean checkPurchaseHistoryDeliveredEmpty();
}
