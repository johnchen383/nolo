package com.example.nolo.fragments;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nolo.R;
import com.example.nolo.activities.ResultActivity;
import com.example.nolo.adaptors.HomeCategoryAdaptor;
import com.example.nolo.adaptors.HomeIndicatorAdaptor;
import com.example.nolo.adaptors.ItemsCompactAdaptor;
import com.example.nolo.adaptors.SearchItemSuggestionAdaptor;
import com.example.nolo.entities.category.ICategory;
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
    private final int SNAP_DURATION = 300;
    private ViewHolder vh;
    private HomeViewModel homeViewModel;
    private View currentView;
    private float historicY = 0;
    private int panelIndex = 0;

    private class ViewHolder {
        ListView categoryList, searchSuggestionsList, indicator;
        LinearLayout initialView, searchLayoutBtn, outsideSearchContainer, browseBtn;
        RecyclerView featuredItemsList;
        TextView featuredText;
        EditText searchBarText;
        ImageView homeLogo, searchBtn, deleteBtn;
        ScrollView scrollView;
        View searchView;

        public ViewHolder(View view) {
            homeLogo = view.findViewById(R.id.home_logo);
            categoryList = view.findViewById(R.id.category_list);
            initialView = view.findViewById(R.id.initial_home_view);
            searchLayoutBtn = view.findViewById(R.id.search_layout_btn);
            featuredItemsList = view.findViewById(R.id.featured_items_list);
            featuredText = view.findViewById(R.id.featured_text);
            outsideSearchContainer = view.findViewById(R.id.outside_search_container);
            scrollView = view.findViewById(R.id.scroll_view);
            browseBtn = view.findViewById(R.id.browse_btn);
            indicator = view.findViewById(R.id.indicator);
            searchView = view.findViewById(R.id.search_view);

            searchBarText = searchView.findViewById(R.id.search_edittext);
            searchBtn = searchView.findViewById(R.id.search_image_btn);
            deleteBtn = searchView.findViewById(R.id.delete_btn);
            searchSuggestionsList = searchView.findViewById(R.id.search_suggestions_list);
        }
    }


    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onResume() {
        super.onResume();
        initAdaptors();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        currentView = view;
        vh = new ViewHolder(view);
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        //set size of initial view to be screen height
        vh.initialView.setMinimumHeight(Display.getScreenHeight(vh.initialView));

        LinearLayout.LayoutParams oldLayout = (LinearLayout.LayoutParams) vh.homeLogo.getLayoutParams();
        LinearLayout.LayoutParams newLayout = new LinearLayout.LayoutParams(oldLayout.width, oldLayout.height);
        newLayout.setMargins(oldLayout.leftMargin, oldLayout.topMargin, oldLayout.rightMargin, (int) (Display.getScreenHeightInDp(view) * 0.2));
        vh.homeLogo.setLayoutParams(newLayout);

        initAdaptors();
        initListeners();
        initStyling();
    }

    private void snapScroll() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(vh.scrollView, "scrollY", vh.scrollView.getScrollY(), Display.getScreenHeight(vh.scrollView) * panelIndex).setDuration(SNAP_DURATION);
        objectAnimator.start();
        updateStyling();
        historicY = Display.getScreenHeight(vh.scrollView) * panelIndex;
    }

    private boolean onTouch(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                historicY = vh.scrollView.getScrollY();
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.AXIS_SIZE:
                float currentY = vh.scrollView.getScrollY();
                if (currentY > historicY) {
                    //swipe down
                    panelIndex++;
                    snapScroll();
                } else if (currentY < historicY) {
                    //swipe up
                    panelIndex--;
                    snapScroll();
                } else {
                    return false;
                }

                historicY = Display.getScreenHeight(vh.scrollView) * panelIndex;
                return true;
        }
        return false;

    }

    private void initStyling() {
        vh.searchSuggestionsList.setMinimumWidth(Display.getScreenWidth(vh.scrollView));
        updateStyling();
    }

    private void updateStyling() {
        if (panelIndex > 0) {
            getActivity().getWindow().setStatusBarColor(Color.argb(255, 0, 0, 0));
            setIndicatorStyles(panelIndex - 1);
        } else {
            getActivity().getWindow().setStatusBarColor(getActivity().getColor(R.color.navy));
            vh.indicator.setVisibility(View.INVISIBLE);
        }
    }

    private void initAdaptors() {
        /**
         * CATEGORY ADAPTOR
         */
        List<ICategory> categories = GetCategoriesUseCase.getCategories();
        HomeCategoryAdaptor categoriesAdaptor = new HomeCategoryAdaptor(getActivity(), R.layout.item_home_category, categories);
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

        /**
         * Indicator adaptor
         */
        List<String> indicatorFields = new ArrayList<>();

        for (int i = 1; i <= categories.size(); i++){
            indicatorFields.add(String.format("%02d  |", i));
        }

        HomeIndicatorAdaptor indicatorAdaptor =
                new HomeIndicatorAdaptor(getContext(), R.layout.item_home_indicator, indicatorFields, (a) -> onClickIndicator(a));

        vh.indicator.setAdapter(indicatorAdaptor);
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
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
            vh.searchBarText.requestFocus();
        });

        // Handle Enter and Back keys
        vh.searchBarText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // When Enter key pressed, go to search list
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    goToSearchActivity(vh.searchBarText.getText().toString());

                    // When Back key pressed, hide the search bar
                } else if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    showSearchContainer(false);
                }

                return false;
            }
        });

        // When search bar has focus, show delete button, otherwise search button
        vh.searchBarText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
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

        vh.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSearchActivity(vh.searchBarText.getText().toString());
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

        vh.browseBtn.setOnClickListener(v -> {
            panelIndex = 1;
            snapScroll();
        });

        vh.scrollView.setOnTouchListener((view1, motionEvent) -> onTouch(motionEvent));
    }

    private void setIndicatorStyles(int index) {
        int opacityNorm = (int) (255 * 0.4);
        int opacitySel = (int) (255 * 0.7);
        vh.indicator.setVisibility(View.VISIBLE);

        for (int pos = 0; pos < vh.indicator.getChildCount(); pos++) {
            RelativeLayout indicatorEl = (RelativeLayout) getViewByPosition(pos, vh.indicator);
            TextView indicatorText = indicatorEl.findViewById(R.id.clickable);

            if (pos == index) {
                indicatorText.setTypeface(indicatorText.getTypeface(), Typeface.BOLD);
                indicatorText.setTextColor(Color.argb(opacitySel, 255, 255, 255));
                continue;
            }

            indicatorText.setTypeface(indicatorText.getTypeface(), Typeface.NORMAL);
            indicatorText.setTextColor(Color.argb(opacityNorm, 255, 255, 255));
        }
    }

    private void onClickIndicator(int index) {
        panelIndex = index + 1;
        snapScroll();
    }

    /**
     * Show/hide the search bar, search suggestions and keyboard
     *
     * @param show boolean - True to go search bar and show keyboard
     *             False to go back the original page and hide keyboard
     */
    private void showSearchContainer(boolean show) {
        if (show) {
            vh.searchView.setVisibility(View.VISIBLE);
            vh.outsideSearchContainer.setVisibility(View.VISIBLE);

            // Show the keyboard
            Keyboard.show(getActivity());
        } else {
            vh.searchView.setVisibility(View.GONE);
            vh.outsideSearchContainer.setVisibility(View.GONE);

            // Hide the keyboard
            Keyboard.hide(getActivity(), currentView);
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

            vh.homeLogo.setAlpha(0.3f);
        } else {
            vh.searchBtn.setVisibility(View.VISIBLE);
            vh.deleteBtn.setVisibility(View.GONE);

            vh.homeLogo.setAlpha(1.0f);
        }
    }

    private int getMaxNumberOfSearchSuggestionsInList() {
        return Display.getScreenHeight(currentView) / 2 / 200;
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
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_up, R.anim.slide_stationery);
        }
    }
}
