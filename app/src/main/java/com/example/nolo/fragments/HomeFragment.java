package com.example.nolo.fragments;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nolo.R;
import com.example.nolo.activities.ResultActivity;
import com.example.nolo.adaptors.HomeCategoryAdaptor;
import com.example.nolo.adaptors.ItemsCompactAdaptor;
import com.example.nolo.entities.category.ICategory;
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
    private final int NUMBER_OF_SEARCH_SUGGESTIONS = 6;
    private final int SNAP_DURATION = 300;
    private ViewHolder vh;
    private HomeViewModel homeViewModel;
    private View currentView;
    private float historicY = 0;
    private int panelIndex = 0;
    private int panelMaxIndex;

    private class ViewHolder {
        ListView categoryList, searchSuggestionsList;
        CardView searchContainer;
        LinearLayout initialView, searchLayoutBtn, searchContainer, outsideSearchContainer, browseBtn, indicator;
        RecyclerView featuredItemsList;
        TextView featuredText, one, two, three;
        EditText searchEditText;
        ImageView searchImageBtn;
        ScrollView scrollView;

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
            scrollView = getView().findViewById(R.id.scroll_view);
            browseBtn = getView().findViewById(R.id.browse_btn);
            indicator = getView().findViewById(R.id.indicator);
            one = getView().findViewById(R.id.one);
            two = getView().findViewById(R.id.two);
            three = getView().findViewById(R.id.three);
        }
    }

    private void setIndicator() {
        int opacityNorm = (int) (255 * 0.4);
        int opacitySel = (int) (255 * 0.7);
        vh.indicator.setVisibility(View.VISIBLE);
        vh.one.setTypeface(vh.one.getTypeface(), Typeface.NORMAL);
        vh.two.setTypeface(vh.two.getTypeface(), Typeface.NORMAL);
        vh.three.setTypeface(vh.three.getTypeface(), Typeface.NORMAL);
        vh.one.setTextColor(Color.argb(opacityNorm, 255, 255, 255));
        vh.two.setTextColor(Color.argb(opacityNorm, 255, 255, 255));
        vh.three.setTextColor(Color.argb(opacityNorm, 255, 255, 255));
        switch (panelIndex) {
            case 1:
                vh.one.setTypeface(vh.three.getTypeface(), Typeface.BOLD);
                vh.one.setTextColor(Color.argb(opacitySel, 255, 255, 255));
                break;
            case 2:
                vh.two.setTypeface(vh.three.getTypeface(), Typeface.BOLD);
                vh.two.setTextColor(Color.argb(opacitySel, 255, 255, 255));
                break;
            case 3:
                vh.three.setTypeface(vh.three.getTypeface(), Typeface.BOLD);
                vh.three.setTextColor(Color.argb(opacitySel, 255, 255, 255));
                break;
            default:
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
        vh.indicator.setVisibility(View.INVISIBLE);

        initAdaptors();
        initListeners();
    }

    private void snapScroll() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(vh.scrollView, "scrollY", vh.scrollView.getScrollY(), Display.getScreenHeight(vh.scrollView) * panelIndex).setDuration(SNAP_DURATION);
        objectAnimator.start();

        if (panelIndex > 0){
            setIndicator();
        } else {
//            Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.home_frag);
//            System.out.println("Frag: " + currentFragment);
//            if (currentFragment instanceof HomeFragment) {
//                FragmentTransaction fragTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                fragTransaction.detach(currentFragment);
//                fragTransaction.attach(currentFragment);
//                fragTransaction.commit();
//            }

            vh.indicator.setVisibility(View.INVISIBLE);
        }

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
                    if (panelIndex > panelMaxIndex) {
                        panelIndex = panelMaxIndex;
                    } else {
                        snapScroll();
                    }
                } else if (currentY < historicY) {
                    //swipe up
                    panelIndex--;
                    if (panelIndex < 0) {
                        panelIndex = 0;
                    } else {
                        snapScroll();
                    }
                }

                historicY = Display.getScreenHeight(vh.scrollView) * panelIndex;
                return true;
        }
        return false;

    }

    private void initAdaptors() {
        /**
         * CATEGORY ADAPTOR
         */
        List<ICategory> categories = GetCategoriesUseCase.getCategories();
        panelMaxIndex = categories.size();
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

        vh.searchEditText.removeTextChangedListener(new TextWatcher() {
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

        vh.searchImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSearchActivity(vh.searchEditText.getText().toString());
            }
        });

        vh.browseBtn.setOnClickListener(v -> {
            panelIndex = 1;
            snapScroll();
        });

        vh.one.setOnClickListener(v -> {
            panelIndex = 1;
            snapScroll();
        });

        vh.two.setOnClickListener(v -> {
            panelIndex = 2;
            snapScroll();
        });

        vh.three.setOnClickListener(v -> {
            panelIndex = 3;
            snapScroll();
        });

        vh.scrollView.setOnTouchListener((view1, motionEvent) -> onTouch(motionEvent));
    }

    /**
     * Show/hide the search bar, search suggestions and keyboard
     *
     * @param show boolean - True to go search bar and show keyboard
     *             False to go back the original page and hide keyboard
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