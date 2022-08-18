package com.example.nolo.interactors.user;

import com.example.nolo.repositories.user.UsersRepository;

import java.util.function.Consumer;

public class SignUpUseCase {
    /**
     * Sign up to the app
     *
     * @param userSignedUp A function that will run after the signup process (both successful and unsuccessful)
     * @param email
     * @param password
     */
    public static void signUp(Consumer<String> userSignedUp, String email, String password) {
        UsersRepository.getInstance().signUp(userSignedUp, email, password);
    }
}
