package com.example.nolo.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.nolo.interactors.user.SignUpUseCase;

import java.util.function.Consumer;

public class SignUpViewModel extends ViewModel implements ISignUpViewModel {
    @Override
    public void signUp(Consumer<String> onUserSignedUp, String email, String password) {
        SignUpUseCase.signUp(onUserSignedUp, email, password);
    }
}
