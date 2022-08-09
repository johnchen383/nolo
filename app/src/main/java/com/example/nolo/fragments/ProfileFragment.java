package com.example.nolo.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.nolo.R;
import com.example.nolo.viewmodels.ProfileViewModel;

public class ProfileFragment extends Fragment {
    private class ViewHolder {
        TextView textView;

        public ViewHolder(){
            textView = getView().findViewById(R.id.text_cart);
        }
    }

    private ViewHolder vh;

    private ProfileViewModel profileViewModel;

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
    }
}