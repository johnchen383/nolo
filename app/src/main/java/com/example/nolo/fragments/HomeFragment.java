package com.example.nolo.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nolo.R;
import com.example.nolo.activities.ResultActivity;
import com.example.nolo.adaptors.HomeCategoryAdaptor;
import com.example.nolo.adaptors.ItemsCompactAdaptor;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.interactors.category.GetCategoriesUseCase;
import com.example.nolo.adaptors.HomeCategoryAdaptor;
import com.example.nolo.adaptors.SearchItemSuggestionAdaptor;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.interactors.category.GetCategoriesUseCase;
import com.example.nolo.interactors.item.GetSearchSuggestionsUseCase;
import com.example.nolo.util.Animation;
import com.example.nolo.util.Display;
import com.example.nolo.util.Keyboard;
import com.example.nolo.util.ListUtil;
import com.example.nolo.viewmodels.HomeViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Fragment to house the home 'tab' on the main activity
 * Used for viewing featured items, browsing categories, and navigation to search
 */
public class HomeFragment extends Fragment {
    private ViewHolder vh;
    private HomeViewModel homeViewModel;
    private View currentView;

    private class ViewHolder {
        ListView categoryList, searchSuggestionsList;
        CardView searchContainer;
        LinearLayout initialView, searchLayoutBtn, outsideSearchContainer;
        RecyclerView featuredItemsList;
        TextView featuredText;
        EditText searchEditText;
        ImageView searchImageBtn;

        public ViewHolder(View view) {
            categoryList = view.findViewById(R.id.category_list);
            initialView = view.findViewById(R.id.initial_home_view);
            searchLayoutBtn = view.findViewById(R.id.search_layout_btn);
            featuredItemsList = view.findViewById(R.id.featured_items_list);
            featuredText = view.findViewById(R.id.featured_text);
            searchEditText = view.findViewById(R.id.search_edittext);
            searchSuggestionsList = view.findViewById(R.id.search_suggestions_list);
            searchContainer = view.findViewById(R.id.search_container);
            outsideSearchContainer = view.findViewById(R.id.outside_search_container);
            searchImageBtn = view.findViewById(R.id.search_image_btn);
        }
    }

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        currentView = view;
        vh = new ViewHolder(view);
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        //set size of initial view to be screen height
        vh.initialView.setMinimumHeight(Display.getScreenHeight(vh.initialView));

        initAdaptors();
        initListeners();
    }

    private void initAdaptors() {
        /**
         * CATEGORY ADAPTOR
         */
        HomeCategoryAdaptor categoriesAdaptor = new HomeCategoryAdaptor(getActivity(), R.layout.item_home_category, GetCategoriesUseCase.getCategories());
        vh.categoryList.setAdapter(categoriesAdaptor);
        ListUtil.setDynamicHeight(vh.categoryList);

        /**
         * FEATURED ITEMS ADAPTOR
         */
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        vh.featuredItemsList.setLayoutManager(layoutManager);

        List<ItemVariant> displayVariants = homeViewModel.getRecentlyViewedItemVariants();

        if (displayVariants.size() <= 0) {
            vh.featuredText.setText(getString(R.string.home_featured_random));
            displayVariants = homeViewModel.generateRandomViewedItemVariants();
        } else {
            vh.featuredText.setText(getString(R.string.home_featured_prev));
        }

        ItemsCompactAdaptor featuredItemsAdaptor = new ItemsCompactAdaptor(getActivity(), displayVariants, 0.43);
        vh.featuredItemsList.setAdapter(featuredItemsAdaptor);
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
        // When outside box is clicked, hide the search related views
        vh.outsideSearchContainer.setOnClickListener(v -> {
            showSearchContainer(false);
        });

        // When first search button is clicked, show search related views
        vh.searchLayoutBtn.setOnClickListener(v -> {
            showSearchContainer(true);
            vh.searchEditText.requestFocus();
        });

        // Handle Enter and Back keys
        vh.searchEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // When Enter key pressed, go to search list
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    goToSearchActivity(vh.searchEditText.getText().toString());

                // When Back key pressed, hide the search bar
                } else if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    showSearchContainer(false);
                }

                return false;
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

        vh.searchImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSearchActivity(vh.searchEditText.getText().toString());
            }
        });
    }

    /**
     * Show/hide the search bar, search suggestions and keyboard
     *
     * @param show boolean - True to go search bar and show keyboard
     *                       False to go back the original page and hide keyboard
     */
    private void showSearchContainer(boolean show) {
        if (show) {
            vh.searchContainer.setVisibility(View.VISIBLE);
            vh.outsideSearchContainer.setVisibility(View.VISIBLE);

            // Show the keyboard
            Keyboard.show(getActivity());
        } else {
            vh.searchContainer.setVisibility(View.GONE);
            vh.outsideSearchContainer.setVisibility(View.GONE);

            // Hide the keyboard
            Keyboard.hide(getActivity(), currentView);
        }
    }

    private int getMaxNumberOfSearchSuggestionsInList() {
        return Display.getScreenHeight(currentView) / 2 / 120;
    }

    private String getColourInHexFromResourceId(int rId) {
        return "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), rId) & 0x00ffffff);
    }

    private void goToSearchActivity(String searchTerm) {
        // Check if Search bar is empty
        if (searchTerm.isEmpty()) {
            Toast.makeText(getActivity(), "Search bar is empty!", Toast.LENGTH_LONG).show();
        } else {
            showSearchContainer(false);

            Intent intent = new Intent(getActivity(), ResultActivity.class);
            intent.putExtra(getString(R.string.search_term), searchTerm);
            startActivity(intent, Animation.Fade(getActivity()).toBundle());
        }
    }
}