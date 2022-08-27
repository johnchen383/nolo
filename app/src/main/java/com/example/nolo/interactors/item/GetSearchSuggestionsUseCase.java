package com.example.nolo.interactors.item;

import com.example.nolo.entities.item.IItem;

import java.util.ArrayList;
import java.util.List;

public class GetSearchSuggestionsUseCase {
    /**
     * Get list of Items by the search terms
     *
     * @param searchTerm Search terms
     * @return List of Items if Items' names contain search term;
     *         Otherwise empty list
     */
    public static List<IItem> getSearchSuggestions(String searchTerm) {
        List<IItem> allItems = GetAllItemsUseCase.getAllItems();
        List<IItem> result = new ArrayList<>();

        for (IItem item : allItems) {
            if (item.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                result.add(item);
            }
        }

        return result;
    }
}
