package com.example.nolo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.nolo.R;
import com.example.nolo.activities.LogInActivity;
import com.example.nolo.interactors.user.GetCurrentUserUseCase;
import com.example.nolo.interactors.user.LogOutUseCase;
import com.example.nolo.util.Animation;
import com.example.nolo.viewmodels.ProfileViewModel;

/**
 * Fragment to house the profile 'tab' on the main activity
 * Used for viewing profile information, modifying profile information and performing profile actions (e.g., logout)
 */
public class ProfileFragment extends Fragment {
    private ViewHolder vh;
    private ProfileViewModel profileViewModel;

    private class ViewHolder {
        TextView textView, emailText;
        Button signOutBtn;

        public ViewHolder() {
            textView = getView().findViewById(R.id.text_profile);
            emailText = getView().findViewById(R.id.text_email);
            signOutBtn = getView().findViewById(R.id.sign_out_button);
        }
    }

    private void initListeners() {
        vh.signOutBtn.setOnClickListener(v -> {
            LogOutUseCase.logOut();
            startActivity(new Intent(getActivity(), LogInActivity.class), Animation.Fade(getActivity()).toBundle());
        });
    }

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        vh = new ViewHolder();
        initListeners();

        String email = GetCurrentUserUseCase.getCurrentUser().getEmail();
        vh.emailText.setText(email);
    }
}