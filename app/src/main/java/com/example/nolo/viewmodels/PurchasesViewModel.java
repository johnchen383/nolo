package com.example.nolo.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.enums.PurchaseStatus;
import com.example.nolo.interactors.user.GetCurrentUserUseCase;
import com.example.nolo.interactors.user.GetPurchasedItemsUseCase;

import java.util.List;
import java.util.stream.Collectors;

public class PurchasesViewModel extends ViewModel implements IPurchasesViewModel {
    @Override
    public List<Purchasable> getUserPurchaseHistoryInTransit() {
        return GetPurchasedItemsUseCase.getPurchaseHistory().stream()
                .filter(o -> o.getStatus().equals(PurchaseStatus.inTransit))
                .collect(Collectors.toList());
    }

    @Override
    public List<Purchasable> getUserPurchaseHistoryDelivered() {
        return GetPurchasedItemsUseCase.getPurchaseHistory().stream()
                .filter(o -> o.getStatus().equals(PurchaseStatus.delivered))
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkPurchaseHistoryInTransitEmpty() {
        return getUserPurchaseHistoryInTransit().isEmpty();
    }

    @Override
    public boolean checkPurchaseHistoryDeliveredEmpty() {
        return getUserPurchaseHistoryDelivered().isEmpty();
    }
}
