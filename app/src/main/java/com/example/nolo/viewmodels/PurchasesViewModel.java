package com.example.nolo.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.enums.PurchaseStatus;
import com.example.nolo.interactors.user.GetCurrentUserUseCase;

import java.util.List;
import java.util.stream.Collectors;

public class PurchasesViewModel extends ViewModel {
    public List<Purchasable> getUserPurchaseHistoryInTransit() {
        return GetCurrentUserUseCase.getCurrentUser().getPurchaseHistory().stream()
                .filter(o -> o.getStatus().equals(PurchaseStatus.inTransit))
                .collect(Collectors.toList());
    }

    public List<Purchasable> getUserPurchaseHistoryDelivered() {
        return GetCurrentUserUseCase.getCurrentUser().getPurchaseHistory().stream()
                .filter(o -> o.getStatus().equals(PurchaseStatus.delivered))
                .collect(Collectors.toList());
    }

}
