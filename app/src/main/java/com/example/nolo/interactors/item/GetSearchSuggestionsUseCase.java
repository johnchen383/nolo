package com.example.nolo.interactors.item;

import com.example.nolo.entities.item.IItem;
import com.example.nolo.repositories.item.ItemsRepository;

import java.util.List;

public class GetSearchSuggestionsUseCase {
    public static List<IItem> getSearchSuggestions(String searchTerm) {
        return ItemsRepository.getInstance().getSearchSuggestions(searchTerm);
    }
}
