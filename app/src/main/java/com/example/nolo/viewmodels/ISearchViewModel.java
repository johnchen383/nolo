package com.example.nolo.viewmodels;

import android.app.Activity;
import android.view.View;

import com.example.nolo.entities.item.IItem;

import java.util.List;

public interface ISearchViewModel {
    List<IItem> getTopSearchSuggestions(String searchTerm, View v);
    String getColourInHexFromResourceId(int rId, Activity activity);
}
