package com.example.nolo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.nolo.R;
import com.example.nolo.activities.ResultActivity;
import com.example.nolo.adaptors.SearchItemSuggestionAdaptor;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.interactors.item.GetSearchSuggestionsUseCase;
import com.example.nolo.util.Animation;
import com.example.nolo.util.Display;
import com.example.nolo.util.Keyboard;
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
    private ViewHolder vh;
    private SearchViewModel searchViewModel;
    private View currentView;

    private class ViewHolder {
        EditText searchEditText;
        ImageView searchBtn, deleteBtn;
        ListView searchSuggestionsList;
        LinearLayout outsideSearchContainer;

        public ViewHolder(View view){
            searchEditText = view.findViewById(R.id.search_edittext);
            searchBtn = view.findViewById(R.id.search_image_btn);
            deleteBtn = view.findViewById(R.id.delete_btn);
            searchSuggestionsList = view.findViewById(R.id.search_suggestions_list);
            outsideSearchContainer = view.findViewById(R.id.outside_search_container);
        }
    }

    public SearchFragment() {
        super(R.layout.fragment_search);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        currentView = view;
        vh = new ViewHolder(view);
        searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        initListeners();
    }

    /**
     * SEARCH SUGGESTION ADAPTOR
     */
    private void resetSearchSuggestionsAdaptor(String searchTerm) {
        List<IItem> firstNItems = new ArrayList<>();

        if (!searchTerm.isEmpty()) {
            // First limit the number of items showing in the list
            List<IItem> searchSuggestions = GetSearchSuggestionsUseCase.getSearchSuggestions(searchTerm);
            firstNItems = searchSuggestions.stream().limit(getMaxNumberOfSearchSuggestionsInList()).collect(Collectors.toList());
        }

        // Create and Set the adaptor
        SearchItemSuggestionAdaptor searchItemSuggestionAdaptor =
                new SearchItemSuggestionAdaptor(getActivity(), R.layout.item_search_suggestion, firstNItems, searchTerm,
                        getColourInHexFromResourceId(R.color.faint_white), getColourInHexFromResourceId(R.color.light_grey));
        vh.searchSuggestionsList.setAdapter(searchItemSuggestionAdaptor);
        ListUtil.setDynamicHeight(vh.searchSuggestionsList);
    }

    private void initListeners() {
        // When Enter is pressed in search bar, go to search result
        vh.searchEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    goToSearchActivity(vh.searchEditText.getText().toString());
                }

                return false;
            }
        });

        // When search bar has focus, show delete button, otherwise search button
        vh.searchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                onSearchBar(hasFocus);
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

        vh.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSearchActivity(vh.searchEditText.getText().toString());
            }
        });

        // When delete button is clicked, remove all text in edit text
        vh.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vh.searchEditText.setText("");
                resetSearchSuggestionsAdaptor(vh.searchEditText.getText().toString());
            }
        });

        vh.outsideSearchContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vh.searchEditText.clearFocus();
                // Hide the keyboard
                Keyboard.hide(getActivity(), currentView);
            }
        });
    }

    /**
     * Show/hide the search & delete button next to search bar
     *
     * @param isOnSearchBar indicate whether it is on search bar or not
     */
    private void onSearchBar(boolean isOnSearchBar) {
        if (isOnSearchBar) {
            vh.searchBtn.setVisibility(View.GONE);
            vh.deleteBtn.setVisibility(View.VISIBLE);
        } else {
            vh.searchBtn.setVisibility(View.VISIBLE);
            vh.deleteBtn.setVisibility(View.GONE);
        }
    }

    private int getMaxNumberOfSearchSuggestionsInList() {
        return Display.getScreenHeight(currentView) / 2 / 100;
    }

    private String getColourInHexFromResourceId(int rId) {
        return "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), rId) & 0x00ffffff);
    }

    private void goToSearchActivity(String searchTerm) {
        // Check if Search bar is empty
        if (searchTerm.isEmpty()) {
            Toast.makeText(getActivity(), "Search bar is empty!", Toast.LENGTH_LONG).show();
        } else {
            // Hide the keyboard
            Keyboard.hide(getActivity(), currentView);

            Intent intent = new Intent(getActivity(), ResultActivity.class);
            intent.putExtra(getString(R.string.search_term), searchTerm);
            startActivity(intent, Animation.Fade(getActivity()).toBundle());
        }
    }
}