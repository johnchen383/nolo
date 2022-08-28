package com.example.nolo.util;

import com.example.nolo.entities.item.IItem;
import com.example.nolo.interactors.item.GetSearchSuggestionsUseCase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchUtil {
    public static List<IItem> getTopSearchSuggestions(String searchTerm, int maxNumOfSearchSuggestions) {
        List<IItem> firstNItems = new ArrayList<>();

        if (!searchTerm.isEmpty()) {
            // First limit the number of items showing in the list
            List<IItem> searchSuggestions = GetSearchSuggestionsUseCase.getSearchSuggestions(searchTerm);
            firstNItems = searchSuggestions.stream().limit(maxNumOfSearchSuggestions).collect(Collectors.toList());
        }

        return firstNItems;
    }
}
