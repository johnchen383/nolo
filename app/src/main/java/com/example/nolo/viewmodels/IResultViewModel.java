package com.example.nolo.viewmodels;

import android.view.View;

import com.example.nolo.entities.item.IItem;

import java.util.List;

public interface IResultViewModel {
    List<IItem> getTopSearchSuggestions(String searchTerm, View v);
}
