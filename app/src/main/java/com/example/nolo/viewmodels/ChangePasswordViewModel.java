package com.example.nolo.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.nolo.interactors.user.ChangePasswordUseCase;

import java.util.function.Consumer;

public class ChangePasswordViewModel extends ViewModel implements IChangePasswordViewModel {
    @Override
    public void changePassword(Consumer<String> onUserChangePassword, String email, String password) {
        ChangePasswordUseCase.changePassword(onUserChangePassword, email, password);
    }
}
