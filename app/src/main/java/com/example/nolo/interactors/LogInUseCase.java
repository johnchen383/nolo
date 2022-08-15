package com.example.nolo.interactors;

import com.example.nolo.repositories.user.UsersRepository;

import java.util.function.Consumer;

public class LogInUseCase {
    public static void logIn(Consumer<String> userLoggedIn, String email, String password) {
        UsersRepository.getInstance().logIn(userLoggedIn, email, password);
    }
}
