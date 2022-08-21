package com.example.nolo.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nolo.R;
import com.example.nolo.adaptors.HomeCategoryAdaptor;
import com.example.nolo.adaptors.HomeFeaturedItemsAdaptor;
import com.example.nolo.adaptors.HomeSearchItemsAdaptor;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.interactors.category.GetCategoriesUseCase;
import com.example.nolo.interactors.item.GetSearchSuggestionsUseCase;
import com.example.nolo.util.Display;
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
    private final int NUMBER_OF_SEARCH_SUGGESTIONS = 6;
    private ViewHolder vh;
    private HomeViewModel homeViewModel;

    private class ViewHolder {
        ListView categoryList, searchSuggestionsList;
        LinearLayout initialView, searchLayoutBtn, searchContainer, outsideSearchContainer;
        RecyclerView featuredItemsList;
        TextView featuredText;
        EditText searchEditText;
        ImageView searchImageBtn;

        public ViewHolder() {
            categoryList = getView().findViewById(R.id.category_list);
            initialView = getView().findViewById(R.id.initial_home_view);
            searchLayoutBtn = getView().findViewById(R.id.search_layout_btn);
            featuredItemsList = getView().findViewById(R.id.featured_items_list);
            featuredText = getView().findViewById(R.id.featured_text);
            searchEditText = getView().findViewById(R.id.search_edittext);
            searchSuggestionsList = getView().findViewById(R.id.search_suggestions_list);
            searchContainer = getView().findViewById(R.id.search_container);
            outsideSearchContainer = getView().findViewById(R.id.outside_search_container);
            searchImageBtn = getView().findViewById(R.id.search_image_btn);
        }
    }

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        vh = new ViewHolder();
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

        HomeFeaturedItemsAdaptor featuredItemsAdaptor = new HomeFeaturedItemsAdaptor(getActivity(), displayVariants);
        vh.featuredItemsList.setAdapter(featuredItemsAdaptor);
    }

    private void resetSearchSuggestionsAdaptor(String searchTerm) {
        /**
         * SEARCH SUGGESTION ADAPTOR
         */
        HomeSearchItemsAdaptor homeSearchItemsAdaptor;
        if (!searchTerm.isEmpty()) {
            // First limit the number of items showing in the list
            List<IItem> searchSuggestions = GetSearchSuggestionsUseCase.getSearchSuggestions(searchTerm);
            List<IItem> firstNItems = searchSuggestions.stream().limit(NUMBER_OF_SEARCH_SUGGESTIONS).collect(Collectors.toList());

            // and then display them
            homeSearchItemsAdaptor = new HomeSearchItemsAdaptor(getActivity(), R.layout.item_home_search_suggestion, firstNItems);
        } else {
            homeSearchItemsAdaptor = new HomeSearchItemsAdaptor(getActivity(), R.layout.item_home_search_suggestion, new ArrayList<>());
        }
        vh.searchSuggestionsList.setAdapter(homeSearchItemsAdaptor);
        ListUtil.setDynamicHeight(vh.searchSuggestionsList);
    }

    private void initListeners() {
        vh.outsideSearchContainer.setOnClickListener(v -> {
            showSearchContainer(false);
        });

        vh.searchLayoutBtn.setOnClickListener(v -> {
            showSearchContainer(true);
            vh.searchEditText.requestFocus();
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
                // Check if Search bar is empty
                if (vh.searchEditText.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Search bar is empty!", Toast.LENGTH_LONG).show();
                } else {
                    String searchTerm = vh.searchEditText.getText().toString();
                    List<IItem> searchSuggestions = GetSearchSuggestionsUseCase.getSearchSuggestions(searchTerm);

                    // Check if Search suggestion is empty
                    if (searchSuggestions.size() <= 0) {
                        Toast.makeText(getActivity(), "Search suggestion is empty!", Toast.LENGTH_LONG).show();
                    } else {
                        // TODO: When search button on the right is pressed,
                        //  go to list view of the search suggestion
                    }
                }
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
            ((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE))
                    .toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        } else {
            vh.searchContainer.setVisibility(View.GONE);
            vh.outsideSearchContainer.setVisibility(View.GONE);

            // Hide the keyboard
            ((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }
}