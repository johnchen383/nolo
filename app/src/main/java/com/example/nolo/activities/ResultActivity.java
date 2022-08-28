package com.example.nolo.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.nolo.R;
import com.example.nolo.adaptors.SearchItemResultAdaptor;
import com.example.nolo.adaptors.SearchItemSuggestionAdaptor;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.interactors.item.GetSearchSuggestionsUseCase;
import com.example.nolo.util.Display;
import com.example.nolo.util.Keyboard;
import com.example.nolo.util.ListUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResultActivity extends BaseActivity {
    private ViewHolder vh;

    private class ViewHolder {
        ScrollView homeScrollView;
        LinearLayout outsideSearchContainer;
        EditText searchBarText;
        ListView searchResultList, searchSuggestionsList;
        TextView numOfResultFound;
        ImageView backBtn, searchBtn, deleteBtn;
        View searchView;

        public ViewHolder() {
            homeScrollView = findViewById(R.id.home_scroll_view);
            outsideSearchContainer = findViewById(R.id.outside_search_container);
            searchResultList = findViewById(R.id.search_results_list);
            numOfResultFound = findViewById(R.id.number_results_found);
            backBtn = findViewById(R.id.back_btn);
            searchView = findViewById(R.id.search_view);

            searchBarText = searchView.findViewById(R.id.search_edittext);
            searchBtn = searchView.findViewById(R.id.search_image_btn);
            deleteBtn = searchView.findViewById(R.id.delete_btn);
            searchSuggestionsList = searchView.findViewById(R.id.search_suggestions_list);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        String searchTerm = getIntent().getExtras().getString(getString(R.string.search_term));

        vh = new ViewHolder();

        initStyle(searchTerm);
        initAdaptors(searchTerm);
        initListeners();
    }

    private void initStyle(String searchTerm) {
        vh.searchBarText.setText(searchTerm);
        vh.searchResultList.setFocusable(false);
        vh.searchSuggestionsList.setVisibility(View.GONE);
    }

    private void initAdaptors(String searchTerm) {
        resetSearchResults(searchTerm);
    }

    /**
     * SEARCH RESULT ADAPTOR
     */
    private void resetSearchResults(String searchTerm) {
        List<IItem> searchResult = new ArrayList<>();

        if (!searchTerm.isEmpty()) {
            searchResult = GetSearchSuggestionsUseCase.getSearchSuggestions(searchTerm);
        }

        // Create and Set the adaptor
        SearchItemResultAdaptor searchItemResultAdaptor =
                new SearchItemResultAdaptor(this, R.layout.item_list_vertical, searchResult);
        vh.searchResultList.setAdapter(searchItemResultAdaptor);
        ListUtil.setDynamicHeight(vh.searchResultList);

        // Set the result found text
        String resultFoundMsg = searchResult.size() + " " + getString(R.string.search_results_found);
        vh.numOfResultFound.setText(resultFoundMsg);
    }

    /**
     * SEARCH SUGGESTION ADAPTOR
     */
    private void resetSearchSuggestionsAdaptor(String searchTerm) {
        List<IItem> firstNItems = new ArrayList<>();

        if (!searchTerm.isEmpty()) {
            List<IItem> searchSuggestions = GetSearchSuggestionsUseCase.getSearchSuggestions(searchTerm);
            firstNItems = searchSuggestions.stream().limit(getMaxNumberOfSearchSuggestionsInList()).collect(Collectors.toList());

        }

        // Create and Set the adaptor
        SearchItemSuggestionAdaptor searchItemSuggestionAdaptor =
                new SearchItemSuggestionAdaptor(this, R.layout.item_search_suggestion, firstNItems, searchTerm,
                        getColourInHexFromResourceId(R.color.faint_white), getColourInHexFromResourceId(R.color.light_grey));
        vh.searchSuggestionsList.setAdapter(searchItemSuggestionAdaptor);
        ListUtil.setDynamicHeight(vh.searchSuggestionsList);
    }

    private void initListeners() {
        // When Enter is pressed in search bar, refresh search result
        vh.searchBarText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    String searchTerm = vh.searchBarText.getText().toString();

                    if (searchTerm.isEmpty()) {
                        Toast.makeText(v.getContext(), "Search bar is empty!", Toast.LENGTH_LONG).show();
                    } else {
                        showSearchSuggestionsList(false, v);
                        resetSearchResults(searchTerm);
                    }
                }
                return false;
            }
        });

        // When search bar has focus, show search suggestions, otherwise no
        vh.searchBarText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showSearchSuggestionsList(true, v);
                    resetSearchSuggestionsAdaptor(vh.searchBarText.getText().toString());
                } else {
                    showSearchSuggestionsList(false, v);
                }
                onSearchBar(hasFocus);
            }
        });

        vh.searchBarText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                resetSearchSuggestionsAdaptor(s.toString());
            }
        });

        vh.searchBarText.removeTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                resetSearchSuggestionsAdaptor(s.toString());
            }
        });

        // When outside box is clicked, hide the search suggestions
        vh.outsideSearchContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchSuggestionsList(false, v);
            }
        });

        // When search button is clicked, show search results
        vh.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = vh.searchBarText.getText().toString();

                if (searchTerm.isEmpty()) {
                    Toast.makeText(v.getContext(), "Search bar is empty!", Toast.LENGTH_LONG).show();
                } else {
                    showSearchSuggestionsList(false, v);
                    resetSearchResults(searchTerm);
                }
            }
        });

        // When delete button is clicked, remove all text in edit text
        vh.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vh.searchBarText.setText("");
                resetSearchSuggestionsAdaptor(vh.searchBarText.getText().toString());
            }
        });

        // When back button is clicked, go back to previous activity
        vh.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String getColourInHexFromResourceId(int rId) {
        return "#" + Integer.toHexString(ContextCompat.getColor(this, rId) & 0x00ffffff);
    }

    private int getMaxNumberOfSearchSuggestionsInList() {
        return Display.getScreenHeight(vh.searchResultList) / 2 / 140;
    }

    /**
     * Show/hide the search bar, search suggestions and keyboard
     *
     * @param show boolean - True to show search suggestions and show keyboard
     *             False to hide search suggestions and hide keyboard
     * @param v    view
     */
    private void showSearchSuggestionsList(boolean show, View v) {
        if (show) {
            vh.searchSuggestionsList.setVisibility(View.VISIBLE);
            vh.outsideSearchContainer.setVisibility(View.VISIBLE);

            // Show the keyboard
            Keyboard.show(this);
        } else {
            vh.searchBarText.clearFocus();
            vh.searchSuggestionsList.setVisibility(View.GONE);
            vh.outsideSearchContainer.setVisibility(View.GONE);

            // Hide the keyboard
            Keyboard.hide(this, v);
        }
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_stationery, R.anim.slide_down);
    }
}