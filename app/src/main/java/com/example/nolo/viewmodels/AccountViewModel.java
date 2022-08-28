package com.example.nolo.viewmodels;

import com.example.nolo.entities.user.IUser;
import com.example.nolo.interactors.user.GetCurrentUserUseCase;
import com.example.nolo.interactors.user.LogOutUseCase;

public class AccountViewModel implements IAccountViewModel {
    @Override
    public String getCurrentUserEmail() {
        return GetCurrentUserUseCase.getCurrentUser().getEmail();
    }

    @Override
    public void logOut() {
        LogOutUseCase.logOut();
    }
}