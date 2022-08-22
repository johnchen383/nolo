package com.example.nolo.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nolo.R;

public class SearchActivity extends BaseActivity {
    private ViewHolder vh;


    private class ViewHolder {
        EditText searchBarText;
        ListView searchResultList;
        TextView numOfResultFound;

        public ViewHolder() {
            searchBarText = findViewById(R.id.search_edittext);
            searchResultList = findViewById(R.id.search_results_list);
            numOfResultFound = findViewById(R.id.number_results_found);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        vh = new ViewHolder();
        initListeners();
    }

    private void initListeners() {

    }
}