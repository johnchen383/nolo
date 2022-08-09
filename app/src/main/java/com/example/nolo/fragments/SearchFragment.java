package com.example.nolo.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.nolo.R;
import com.example.nolo.viewmodels.SearchViewModel;

public class SearchFragment extends Fragment {
    private SearchViewModel searchViewModel;

    public SearchFragment() {
        super(R.layout.fragment_search);
        System.out.println("IN SEARCH");
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);
    }
}