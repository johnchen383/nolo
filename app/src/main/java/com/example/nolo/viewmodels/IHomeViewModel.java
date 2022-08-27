package com.example.nolo.viewmodels;

import com.example.nolo.entities.item.variant.ItemVariant;

import java.util.List;

public interface IHomeViewModel {
    List<ItemVariant> getRecentlyViewedItemVariants();
    List<ItemVariant> generateRandomViewedItemVariants();
}
