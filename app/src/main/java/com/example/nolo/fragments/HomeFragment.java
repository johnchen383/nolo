package com.example.nolo.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.nolo.R;
import com.example.nolo.viewmodels.HomeViewModel;

public class HomeFragment extends Fragment {
    private class ViewHolder {
        TextView textView;

        public ViewHolder(){
            textView = getView().findViewById(R.id.text_cart);
        }
    }

    private ViewHolder vh;

    private HomeViewModel homeViewModel;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
    }
}