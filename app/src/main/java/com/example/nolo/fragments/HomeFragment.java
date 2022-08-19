package com.example.nolo.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.nolo.R;
import com.example.nolo.adaptors.HomeCategoryAdaptor;
import com.example.nolo.interactors.category.GetCategoriesUseCase;
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

        public ViewHolder(){
            categoryList = getView().findViewById(R.id.category_list);
        }
    }

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        vh = new ViewHolder();

        HomeCategoryAdaptor categoriesAdaptor = new HomeCategoryAdaptor(getActivity(), R.layout.item_home_category, GetCategoriesUseCase.getCategories());
        vh.categoryList.setAdapter(categoriesAdaptor);
    }
}