package com.example.nolo.interactors.item;

import com.example.nolo.entities.item.IItem;
import com.example.nolo.repositories.item.ItemsRepository;

import java.util.List;

public class GetSearchSuggestionsUseCase {
    public static List<IItem> getSearchSuggestions(String searchTerm) {
        /**
         * Get list of Items by the search terms
         *
         * @param searchTerm Search terms
         * @return List of Items if Items' names contain search term;
         *         Otherwise empty list
         */
        return ItemsRepository.getInstance().getSearchSuggestions(searchTerm);
    }
}
