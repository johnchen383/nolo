package com.example.nolo.viewmodels;

import java.util.function.Consumer;

public interface ILogInViewModel {
    void logIn(Consumer<String> userLoggedIn, String email, String password);
}
