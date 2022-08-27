package com.example.nolo.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.nolo.interactors.user.ChangePasswordUseCase;
import com.example.nolo.interactors.user.SignUpUseCase;

import java.util.function.Consumer;

public class ChangePasswordViewModel extends ViewModel {

    public void changePassword(Consumer<String> onUserChangePassword, String email, String password) {
        ChangePasswordUseCase.changePassword(onUserChangePassword, email, password);
    }
}
