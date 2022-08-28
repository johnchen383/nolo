package com.example.nolo.viewmodels;

import android.view.View;

import com.example.nolo.entities.category.ICategory;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.entities.item.variant.ItemVariant;

import java.util.List;

public interface IHomeViewModel {
    List<ItemVariant> getRecentlyViewedItemVariants();
    List<ItemVariant> generateRandomViewedItemVariants();
    List<ICategory> getCategoryTypes();
    List<String> getIndicatorFields();
    List<IItem> getTopSearchSuggestions(String searchTerm, View v);
}
