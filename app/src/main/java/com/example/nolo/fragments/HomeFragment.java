package com.example.nolo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nolo.R;
import com.example.nolo.activities.MainActivity;
import com.example.nolo.activities.SearchActivity;
import com.example.nolo.adaptors.HomeCategoryAdaptor;
import com.example.nolo.adaptors.HomeFeaturedItemsAdaptor;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.entities.item.colour.Colour;
import com.example.nolo.entities.item.specs.specsoption.SpecsOption;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.entities.user.User;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.interactors.category.GetCategoriesUseCase;
import com.example.nolo.interactors.item.GetAllItemsUseCase;
import com.example.nolo.interactors.store.GetStoreByIdUseCase;
import com.example.nolo.interactors.user.AddViewedItemUseCase;
import com.example.nolo.interactors.user.GetRecentViewedItemsUseCase;
import com.example.nolo.util.Animation;
import com.example.nolo.util.Display;
import com.example.nolo.util.ListUtil;
import com.example.nolo.viewmodels.HomeViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Fragment to house the home 'tab' on the main activity
 * Used for viewing featured items, browsing categories, and navigation to search
 */
public class HomeFragment extends Fragment {
    private ViewHolder vh;
    private HomeViewModel homeViewModel;

    private class ViewHolder {
        ListView categoryList;
        LinearLayout initialView;
        LinearLayout searchBtn;
        RecyclerView featuredItemsList;

        public ViewHolder() {
            categoryList = getView().findViewById(R.id.category_list);
            initialView = getView().findViewById(R.id.initial_home_view);
            searchBtn = getView().findViewById(R.id.search_layout_btn);
            featuredItemsList = getView().findViewById(R.id.featured_items_list);
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

        List<IItemVariant> vHist = GetRecentViewedItemsUseCase.getRecentViewedItems();

        if (vHist.size() <= 0){
            //replace with random selection and update text
            vHist = new ArrayList<>();

            List<IItem> items = GetAllItemsUseCase.getAllItems();
            int increment = items.size() / (User.MAX_VIEWED + 2);
            int seedPosition = (int)(Math.random() * items.size());

            for (int i = 0; i < User.MAX_VIEWED; i++){
                IItem itemToAdd = items.get((seedPosition + (i * increment)) % items.size());
                vHist.add(itemToAdd.getDefaultItemVariant());
            }
        }

        HomeFeaturedItemsAdaptor featuredItemsAdaptor = new HomeFeaturedItemsAdaptor(getActivity(), vHist);
        vh.featuredItemsList.setAdapter(featuredItemsAdaptor);
    }

    private void initListeners() {
        vh.searchBtn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SearchActivity.class), Animation.Fade(getActivity()).toBundle());
        });
    }
}