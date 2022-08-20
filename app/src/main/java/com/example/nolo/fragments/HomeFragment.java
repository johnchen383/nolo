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
        HomeCategoryAdaptor categoriesAdaptor = new HomeCategoryAdaptor(getActivity(), R.layout.item_home_category, GetCategoriesUseCase.getCategories());
        vh.categoryList.setAdapter(categoriesAdaptor);
        ListUtil.setDynamicHeight(vh.categoryList);

        //populate
        IItem item = GetAllItemsUseCase.getAllItems().get(0);
        String ite1 = item.getItemId();
        String str1 = item.getStoreVariants().get(0).getStoreId();
        IItemVariant i1 = new ItemVariant(null, ite1, CategoryType.laptops, str1, null, null, null);


        IItem item2 = GetAllItemsUseCase.getAllItems().get(1);
        String ite2 = item2.getItemId();
        String str2 = item2.getStoreVariants().get(0).getStoreId();
        IItemVariant i2 = new ItemVariant(null, ite2, CategoryType.laptops, str2, null, null, null);

        IItem item3 = GetAllItemsUseCase.getAllItems().get(2);
        String ite3 = item3.getItemId();
        String str3 = item3.getStoreVariants().get(0).getStoreId();
        IItemVariant i3 = new ItemVariant(null, ite3, CategoryType.laptops, str3, null, null, null);

        AddViewedItemUseCase.addViewHistory(i1);
        AddViewedItemUseCase.addViewHistory(i2);
        AddViewedItemUseCase.addViewHistory(i3);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        vh.featuredItemsList.setLayoutManager(layoutManager);
        HomeFeaturedItemsAdaptor featuredItemsAdaptor = new HomeFeaturedItemsAdaptor(getActivity(), GetRecentViewedItemsUseCase.getRecentViewedItems());
        vh.featuredItemsList.setAdapter(featuredItemsAdaptor);
    }

    private void initListeners() {
        vh.searchBtn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SearchActivity.class), Animation.Fade(getActivity()).toBundle());
        });
    }
}