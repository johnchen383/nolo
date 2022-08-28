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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.nolo.R;
import com.example.nolo.activities.MainActivity;
import com.example.nolo.activities.ResultActivity;
import com.example.nolo.adaptors.SearchItemSuggestionAdaptor;
import com.example.nolo.util.ColourUtil;
import com.example.nolo.util.Keyboard;
import com.example.nolo.util.ListUtil;
import com.example.nolo.viewmodels.ISearchViewModel;
import com.example.nolo.viewmodels.SearchViewModel;

/**
 * Fragment to house the search 'tab' on the main activity
 * Used to search for a given item by search query. Filtering of search results is also supported
 */
public class SearchFragment extends Fragment {
    private ISearchViewModel searchViewModel;
    private ViewHolder vh;
    private View currentView;

    private class ViewHolder {
        EditText searchBarText;
        ImageView searchLogo, searchBtn, deleteBtn;
        ListView searchSuggestionsList;
        LinearLayout outsideSearchContainer;
        View searchView;

        public ViewHolder(View view) {
            searchLogo = view.findViewById(R.id.search_logo);
            outsideSearchContainer = view.findViewById(R.id.outside_search_container);
            searchView = view.findViewById(R.id.search_view);

            searchBarText = searchView.findViewById(R.id.search_edittext);
            searchBtn = searchView.findViewById(R.id.search_image_btn);
            deleteBtn = searchView.findViewById(R.id.delete_btn);
            searchSuggestionsList = searchView.findViewById(R.id.search_suggestions_list);
        }
    }

    public SearchFragment() {
        super(R.layout.fragment_search);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        currentView = view;
        vh = new ViewHolder(view);

        // Initialisation
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    /**
     * Initialisation
     */
    private void init() {
        ((MainActivity) getActivity()).updateCartBadge();

        initStyling();
        initListeners();
    }

    private void initStyling() {
        getActivity().getWindow().setStatusBarColor(getActivity().getColor(R.color.navy));
        vh.searchSuggestionsList.setVisibility(View.GONE);
    }

    private void initListeners() {
        // When Enter is pressed in search bar, go to search result
        vh.searchBarText.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                goToSearchActivity(vh.searchBarText.getText().toString());
            }
            return false;
        });

        // When search bar has focus, show delete button, otherwise search button
        vh.searchBarText.setOnFocusChangeListener((v, hasFocus) -> {
            onSearchBar(hasFocus);
        });

        vh.searchBarText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                resetSearchSuggestionsAdaptor(s.toString());
            }
        });

        vh.searchBarText.removeTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                resetSearchSuggestionsAdaptor(s.toString());
            }
        });

        // When search button is clicked
        vh.searchBtn.setOnClickListener(v -> {
            goToSearchActivity(vh.searchBarText.getText().toString());
        });

        // When delete button is clicked, remove all text in edit text
        vh.deleteBtn.setOnClickListener(v -> {
            vh.searchBarText.setText("");
            resetSearchSuggestionsAdaptor(vh.searchBarText.getText().toString());
        });

        // When outside of the search bar is clicked
        vh.outsideSearchContainer.setOnClickListener(v -> {
            vh.searchBarText.clearFocus();
            // Hide the keyboard
            Keyboard.hide(getActivity(), currentView);
        });
    }

    /**
     * SEARCH SUGGESTION ADAPTOR
     */
    private void resetSearchSuggestionsAdaptor(String searchTerm) {
        // Create and Set the adaptor
        SearchItemSuggestionAdaptor searchItemSuggestionAdaptor =
                new SearchItemSuggestionAdaptor(getActivity(), R.layout.item_search_suggestion,
                        searchViewModel.getTopSearchSuggestions(searchTerm, currentView),
                        searchTerm,
                        ColourUtil.getColourInHexFromResourceId(R.color.faint_white, getActivity()),
                        ColourUtil.getColourInHexFromResourceId(R.color.light_grey, getActivity()));
        vh.searchSuggestionsList.setAdapter(searchItemSuggestionAdaptor);
        ListUtil.setDynamicHeight(vh.searchSuggestionsList);
    }

    /**
     * Show/hide the search & delete button next to search bar
     *
     * @param isOnSearchBar indicate whether it is on search bar or not
     */
    private void onSearchBar(boolean isOnSearchBar) {
        vh.searchBtn.setVisibility(isOnSearchBar ? View.GONE : View.VISIBLE);
        vh.deleteBtn.setVisibility(isOnSearchBar ? View.VISIBLE : View.GONE);
        vh.searchSuggestionsList.setVisibility(isOnSearchBar ? View.VISIBLE : View.GONE);
        vh.searchLogo.setAlpha(isOnSearchBar ? 0.3f : 1.0f);
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
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_up, R.anim.slide_stationery);
        }
    }
}