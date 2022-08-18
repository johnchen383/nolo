package com.example.nolo.interactors.user;

import com.example.nolo.repositories.user.UsersRepository;

import java.util.function.Consumer;

public class LogInUseCase {
    /**
     *
     * @param userLoggedIn
     * @param email
     * @param password
     */
    public static void logIn(Consumer<String> userLoggedIn, String email, String password) {
        UsersRepository.getInstance().logIn(userLoggedIn, email, password);
    }
}
