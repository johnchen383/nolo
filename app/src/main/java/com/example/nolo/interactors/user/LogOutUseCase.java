package com.example.nolo.interactors.user;

import com.example.nolo.repositories.user.UsersRepository;

public class LogOutUseCase {
    /**
     * Log out of the app
     */
    public static void logOut() {
        UsersRepository.getInstance().logOut();
    }
}
