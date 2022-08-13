package com.example.nolo.interactors;

import com.example.nolo.repositories.user.UsersRepository;

public class SignUpUseCase {
    public static void signUp(String email, String password) {
        UsersRepository.getInstance().signUp(email, password);
    }
}
