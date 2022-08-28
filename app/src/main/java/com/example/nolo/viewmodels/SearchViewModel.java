package com.example.nolo.viewmodels;

import android.app.Activity;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;

import com.example.nolo.entities.item.IItem;
import com.example.nolo.interactors.item.GetSearchSuggestionsUseCase;
import com.example.nolo.util.Display;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchViewModel extends ViewModel implements ISearchViewModel {
    private int getMaxNumberOfSearchSuggestionsInList(View v) {
        return Display.getScreenHeight(v) / 2 / 100;
    }

    @Override
    public List<IItem> getTopSearchSuggestions(String searchTerm, View v) {
        List<IItem> firstNItems = new ArrayList<>();

        if (!searchTerm.isEmpty()) {
            // First limit the number of items showing in the list
            List<IItem> searchSuggestions = GetSearchSuggestionsUseCase.getSearchSuggestions(searchTerm);
            firstNItems = searchSuggestions.stream().limit(getMaxNumberOfSearchSuggestionsInList(v)).collect(Collectors.toList());
        }

        return firstNItems;
    }

    @Override
    public String getColourInHexFromResourceId(int rId, Activity activity) {
        return "#" + Integer.toHexString(ContextCompat.getColor(activity, rId) & 0x00ffffff);
    }
}
