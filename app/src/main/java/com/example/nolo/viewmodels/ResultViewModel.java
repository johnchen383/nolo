package com.example.nolo.viewmodels;

import android.view.View;

import com.example.nolo.entities.item.IItem;
import com.example.nolo.util.Display;
import com.example.nolo.util.SearchUtil;

import java.util.List;

public class ResultViewModel implements IResultViewModel {
    private int getMaxNumberOfSearchSuggestionsInList(View v) {
        return Display.getScreenHeight(v) / 2 / 140;
    }

    @Override
    public List<IItem> getTopSearchSuggestions(String searchTerm, View v) {
        return SearchUtil.getTopSearchSuggestions(searchTerm, getMaxNumberOfSearchSuggestionsInList(v));
    }
}
