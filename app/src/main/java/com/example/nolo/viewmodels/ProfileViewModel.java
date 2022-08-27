package com.example.nolo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.nolo.interactors.user.LogOutUseCase;

public class ProfileViewModel extends ViewModel {

    public void logOut() {
        LogOutUseCase.logOut();
    }
}
