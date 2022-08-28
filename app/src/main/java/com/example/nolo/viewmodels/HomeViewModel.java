package com.example.nolo.viewmodels;

import android.view.View;

import androidx.lifecycle.ViewModel;

import com.example.nolo.entities.category.ICategory;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.entities.user.User;
import com.example.nolo.interactors.category.GetCategoriesUseCase;
import com.example.nolo.interactors.item.GetAllItemsUseCase;
import com.example.nolo.interactors.user.GetRecentViewedItemsUseCase;
import com.example.nolo.util.Display;
import com.example.nolo.util.SearchUtil;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel implements IHomeViewModel {
    public HomeViewModel() {
    }

    @Override
    public List<ItemVariant> getRecentlyViewedItemVariants() {
        return GetRecentViewedItemsUseCase.getRecentViewedItems();
    }

    @Override
    public List<ItemVariant> generateRandomViewedItemVariants() {
        List<ItemVariant> vHist = new ArrayList<>();
        List<IItem> items = GetAllItemsUseCase.getAllItems();

        int increment = items.size() / (User.MAX_VIEWED + 2);
        int seedPosition = (int) (Math.random() * items.size());

        for (int i = 0; i < User.MAX_VIEWED; i++) {
            int pos = (seedPosition + (i * increment)) % items.size();
            IItem itemToAdd = items.get(pos);
            vHist.add((ItemVariant) itemToAdd.getDefaultItemVariant());
        }

        return vHist;
    }

    @Override
    public List<ICategory> getCategoryTypes() {
        return GetCategoriesUseCase.getCategories();
    }

    @Override
    public List<String> getIndicatorFields() {
        List<String> indicatorFields = new ArrayList<>();

        for (int i = 1; i <= getCategoryTypes().size(); i++) {
            indicatorFields.add(String.format("%02d  |", i));
        }

        return indicatorFields;
    }

    private int getMaxNumberOfSearchSuggestionsInList(View v) {
        return Display.getScreenHeight(v) / 2 / 200;
    }

    @Override
    public List<IItem> getTopSearchSuggestions(String searchTerm, View v) {
        return SearchUtil.getTopSearchSuggestions(searchTerm, getMaxNumberOfSearchSuggestionsInList(v));
    }
}
