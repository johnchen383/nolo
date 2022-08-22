package com.example.nolo.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.nolo.R;
import com.example.nolo.adaptors.SearchItemResultAdaptor;
import com.example.nolo.adaptors.SearchItemSuggestionAdaptor;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.interactors.item.GetSearchSuggestionsUseCase;
import com.example.nolo.util.Keyboard;
import com.example.nolo.util.ListUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResultActivity extends BaseActivity {
    private ViewHolder vh;

    private class ViewHolder {
        EditText searchBarText;
        ListView searchResultList, searchSuggestionsList;
        TextView numOfResultFound;
        ImageView backBtn;

        public ViewHolder() {
            searchBarText = findViewById(R.id.search_edittext);
            searchResultList = findViewById(R.id.search_results_list);
            searchSuggestionsList = findViewById(R.id.search_suggestions_list);
            numOfResultFound = findViewById(R.id.number_results_found);
            backBtn = findViewById(R.id.back_btn);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        String searchTerm = getIntent().getExtras().getString(getString(R.string.search_term));

        vh = new ViewHolder();
        vh.searchBarText.setText(searchTerm);

        initAdaptors(searchTerm);
        initListeners();
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

        // TODO: change the R.layout.(list itme), use the same xml as the accessory list
        // Create and Set the adaptor
        SearchItemResultAdaptor searchItemResultAdaptor =
                new SearchItemResultAdaptor(this, R.layout.item_search_list, searchResult);
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
        List<IItem> searchSuggestions = new ArrayList<>();

        if (!searchTerm.isEmpty()) {
            searchSuggestions = GetSearchSuggestionsUseCase.getSearchSuggestions(searchTerm);
        }

        // Create and Set the adaptor
        SearchItemSuggestionAdaptor searchItemSuggestionAdaptor =
                new SearchItemSuggestionAdaptor(this, R.layout.item_search_suggestion, searchSuggestions, searchTerm,
                        getColourInHexFromResourceId(R.color.faint_white), getColourInHexFromResourceId(R.color.light_grey));
        vh.searchSuggestionsList.setAdapter(searchItemSuggestionAdaptor);
        ListUtil.setDynamicHeight(vh.searchSuggestionsList);
    }

    private void initListeners() {
        Activity currentActivity = this;

        vh.searchBarText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // When enter is pressed
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    resetSearchResults(vh.searchBarText.getText().toString());
                    Keyboard.hide(currentActivity, v);
                }
                return false;
            }
        });

        vh.searchBarText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    vh.searchSuggestionsList.setVisibility(View.VISIBLE);
                    resetSearchSuggestionsAdaptor(vh.searchBarText.getText().toString());
                } else {
                    vh.searchSuggestionsList.setVisibility(View.GONE);
                    Keyboard.hide(currentActivity, v);
                }
            }
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
}