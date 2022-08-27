package com.example.nolo.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.nolo.interactors.user.LogInUseCase;

import java.util.function.Consumer;


public class LogInViewModel extends ViewModel implements ILogInViewModel {
    @Override
    public void logIn(Consumer<String> userLoggedIn, String email, String password) {
        LogInUseCase.logIn(userLoggedIn, email, password);
    }
}
