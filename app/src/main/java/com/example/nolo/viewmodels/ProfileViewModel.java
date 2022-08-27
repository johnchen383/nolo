package com.example.nolo.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.nolo.interactors.user.LogOutUseCase;

public class ProfileViewModel extends ViewModel implements IProfileViewModel {
    @Override
    public void logOut() {
        LogOutUseCase.logOut();
    }
}
