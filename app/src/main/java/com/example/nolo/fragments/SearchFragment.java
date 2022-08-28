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
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.nolo.R;
import com.example.nolo.activities.MainActivity;
import com.example.nolo.activities.ResultActivity;
import com.example.nolo.adaptors.SearchItemSuggestionAdaptor;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.interactors.item.GetSearchSuggestionsUseCase;
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
        EditText searchBarText;
        RelativeLayout searchBtn, deleteBtn;
        ImageView searchLogo, searchButtonImage, deleteButtonImage;
        ListView searchSuggestionsList;
        LinearLayout outsideSearchContainer;
        View searchView;

        public ViewHolder(View view){
            searchLogo = view.findViewById(R.id.search_logo);
            outsideSearchContainer = view.findViewById(R.id.outside_search_container);
            searchView = view.findViewById(R.id.search_view);

            searchBarText = searchView.findViewById(R.id.search_edittext);
            searchBtn = searchView.findViewById(R.id.search_btn);
            searchButtonImage = searchView.findViewById(R.id.search_image_btn);
            deleteBtn = searchView.findViewById(R.id.delete_btn);
            deleteButtonImage = searchView.findViewById(R.id.delete_image_btn);
            searchSuggestionsList = searchView.findViewById(R.id.search_suggestions_list);
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

        ((MainActivity) getActivity()).updateCartBadge();

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
        vh.searchSuggestionsList.setVisibility(View.GONE);

        // When Enter is pressed in search bar, go to search result
        vh.searchBarText.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                goToSearchActivity(vh.searchBarText.getText().toString());
            }

            return false;
        });

        // When search bar has focus, show delete button, otherwise search button
        vh.searchBarText.setOnFocusChangeListener((v, hasFocus) -> onSearchBar(hasFocus));

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

        vh.searchButtonImage.setOnClickListener(v -> goToSearchActivity(vh.searchBarText.getText().toString()));
        vh.searchBtn.setOnClickListener(v -> goToSearchActivity(vh.searchBarText.getText().toString()));

        // When delete button is clicked, remove all text in edit text
        vh.deleteButtonImage.setOnClickListener(v -> {
            onClickDelete(vh);
        });

        vh.deleteBtn.setOnClickListener(v -> {
            onClickDelete(vh);
        });

        vh.outsideSearchContainer.setOnClickListener(v -> {
            vh.searchBarText.clearFocus();
            // Hide the keyboard
            Keyboard.hide(getActivity(), currentView);
        });
    }

    private void onClickDelete(ViewHolder vh) {
        vh.searchBarText.setText("");
        resetSearchSuggestionsAdaptor(vh.searchBarText.getText().toString());
    }

    /**
     * Show/hide the search & delete button next to search bar
     *
     * @param isOnSearchBar indicate whether it is on search bar or not
     */
    private void onSearchBar(boolean isOnSearchBar) {
        if (isOnSearchBar) {
            vh.searchBtn.setVisibility(View.GONE);
            vh.searchButtonImage.setVisibility(View.GONE);
            vh.deleteBtn.setVisibility(View.VISIBLE);
            vh.deleteButtonImage.setVisibility(View.VISIBLE);
            vh.searchSuggestionsList.setVisibility(View.VISIBLE);
            vh.searchLogo.setAlpha(0.3f);
        } else {
            vh.searchBtn.setVisibility(View.VISIBLE);
            vh.searchButtonImage.setVisibility(View.VISIBLE);
            vh.deleteBtn.setVisibility(View.GONE);
            vh.deleteButtonImage.setVisibility(View.GONE);
            vh.searchSuggestionsList.setVisibility(View.GONE);
            vh.searchLogo.setAlpha(1.0f);
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
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_up, R.anim.slide_stationery);
        }
    }
}