package com.example.nolo.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.nolo.R;
import com.example.nolo.adaptors.HomeSearchItemsAdaptor;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.interactors.item.GetSearchSuggestionsUseCase;
import com.example.nolo.util.ListUtil;
import com.example.nolo.viewmodels.SearchViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Fragment to house the search 'tab' on the main activity
 * Used to search for a given item by search query. Filtering of search results is also supported
 */
public class SearchFragment extends Fragment {
    private final int MAX_NUMBER_OF_SEARCH_SUGGESTIONS = 10;
    private ViewHolder vh;
    private SearchViewModel searchViewModel;

    private class ViewHolder {
        LinearLayout outsideSearchContainer;
        RelativeLayout searchLogo;
        EditText searchEditText;
        ListView searchSuggestionsList;

        public ViewHolder(){
            outsideSearchContainer = getView().findViewById(R.id.search_fragment_outside_search_container);
            searchLogo = getView().findViewById(R.id.search_logo);
            searchEditText = getView().findViewById(R.id.search_fragment_edittext);
            searchSuggestionsList = getView().findViewById(R.id.search_fragment_suggestions_list);
        }
    }

    public SearchFragment() {
        super(R.layout.fragment_search);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        vh = new ViewHolder();
        searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        initListeners();
    }

    private void resetSearchSuggestionsAdaptor(String searchTerm) {
        /**
         * SEARCH SUGGESTION ADAPTOR
         */
        HomeSearchItemsAdaptor homeSearchItemsAdaptor;
        if (!searchTerm.isEmpty()) {
            // First limit the number of items showing in the list
            List<IItem> searchSuggestions = GetSearchSuggestionsUseCase.getSearchSuggestions(searchTerm);
            List<IItem> firstNItems = searchSuggestions.stream().limit(MAX_NUMBER_OF_SEARCH_SUGGESTIONS).collect(Collectors.toList());

            // and then display them
            homeSearchItemsAdaptor = new HomeSearchItemsAdaptor(getActivity(), R.layout.item_search_suggestion, firstNItems,
                    searchTerm, "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.faint_white) & 0x00ffffff),
                    "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.light_grey) & 0x00ffffff));
        } else {
            homeSearchItemsAdaptor = new HomeSearchItemsAdaptor(getActivity(), R.layout.item_search_suggestion, new ArrayList<>(),
                    searchTerm, "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.faint_white) & 0x00ffffff),
                    "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.light_grey) & 0x00ffffff));
        }
        vh.searchSuggestionsList.setAdapter(homeSearchItemsAdaptor);
        ListUtil.setDynamicHeight(vh.searchSuggestionsList);
    }

    private void initListeners() {
//        vh.outsideSearchContainer.setOnClickListener(v -> showSearchLogo(true));

        vh.searchEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showSearchLogo(false);
            }
        });

        vh.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                resetSearchSuggestionsAdaptor(s.toString());
            }
        });

        vh.searchEditText.removeTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                resetSearchSuggestionsAdaptor(s.toString());
            }
        });
    }

    /**
     * Show/hide the search bar, search suggestions and keyboard
     *
     * @param show boolean - True to go search bar and show keyboard
     *                       False to go back the original page and hide keyboard
     */
    private void showSearchLogo(boolean show) {
        if (show) {
            vh.searchLogo.setVisibility(View.VISIBLE);
            vh.outsideSearchContainer.setVisibility(View.GONE);
        } else {
            vh.searchLogo.setVisibility(View.GONE);
            vh.outsideSearchContainer.setVisibility(View.VISIBLE);
        }
    }
}