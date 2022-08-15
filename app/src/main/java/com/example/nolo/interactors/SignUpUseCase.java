package com.example.nolo.interactors;

import com.example.nolo.repositories.user.UsersRepository;

import java.util.function.Consumer;

public class SignUpUseCase {
    public static void signUp(Consumer<String> userSignedUp, String email, String password) {
        UsersRepository.getInstance().signUp(userSignedUp, email, password);
    }
}
