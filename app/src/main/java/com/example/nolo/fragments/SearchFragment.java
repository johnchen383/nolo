package com.example.nolo.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.nolo.R;
import com.example.nolo.viewmodels.SearchViewModel;

public class SearchFragment extends Fragment {
    private ViewHolder vh;
    private SearchViewModel searchViewModel;

    private class ViewHolder {
        TextView textView;

        public ViewHolder(){
            textView = getView().findViewById(R.id.text_search);
        }
    }

    public SearchFragment() {
        super(R.layout.fragment_search);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);
    }
}