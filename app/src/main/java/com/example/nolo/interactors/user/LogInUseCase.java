package com.example.nolo.interactors.user;

import com.example.nolo.repositories.user.UsersRepository;

import java.util.function.Consumer;

public class LogInUseCase {
    /**
     * Log in to the app
     *
     * @param onUserLoggedIn A function that will run after the login process (both successful and unsuccessful)
     * @param email
     * @param password
     */
    public static void logIn(Consumer<String> onUserLoggedIn, String email, String password) {
        UsersRepository.getInstance().logIn(onUserLoggedIn, email, password);
    }
}
