package com.example.nolo.interactors.user;

import com.example.nolo.repositories.user.UsersRepository;

public class LogOutUseCase {
    /**
     *
     */
    public static void logOut() {
        UsersRepository.getInstance().logOut();
    }
}
