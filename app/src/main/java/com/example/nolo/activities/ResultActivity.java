package com.example.nolo.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nolo.R;
import com.example.nolo.adaptors.SearchItemResultAdaptor;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.interactors.item.GetSearchSuggestionsUseCase;
import com.example.nolo.util.Keyboard;
import com.example.nolo.util.ListUtil;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends BaseActivity {
    private ViewHolder vh;

    private class ViewHolder {
        EditText searchBarText;
        ListView searchResultList;
        TextView numOfResultFound;
        ImageView backBtn;

        public ViewHolder() {
            searchBarText = findViewById(R.id.search_edittext);
            searchResultList = findViewById(R.id.search_results_list);
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

    private void resetSearchResults(String searchTerm) {
        /**
         * SEARCH SUGGESTION ADAPTOR
         */
        SearchItemResultAdaptor searchItemResultAdaptor;
        List<IItem> searchResult = new ArrayList<>();

        if (!searchTerm.isEmpty()) {
            // First limit the number of items showing in the list
            searchResult = GetSearchSuggestionsUseCase.getSearchSuggestions(searchTerm);
        }

        // Create and Set the adaptor
        searchItemResultAdaptor = new SearchItemResultAdaptor(this, R.layout.item_search_list, searchResult);
        vh.searchResultList.setAdapter(searchItemResultAdaptor);
        ListUtil.setDynamicHeight(vh.searchResultList);

        // Set the result found text
        String resultFoundMsg = searchResult.size() + " " + getString(R.string.search_results_found);
        vh.numOfResultFound.setText(resultFoundMsg);
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

        vh.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}