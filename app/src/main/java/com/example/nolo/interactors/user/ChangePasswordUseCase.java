package com.example.nolo.interactors.user;

import com.example.nolo.repositories.user.UsersRepository;

import java.util.function.Consumer;

public class ChangePasswordUseCase {
    /**
     * Change user's password after log in to the app
     *
     * @param onUserChangePassword A function that will run after changing password (both successful and unsuccessful)
     * @param oldPassword
     * @param newPassword
     */
    public static void changePassword(Consumer<String> onUserChangePassword, String oldPassword, String newPassword) {
        UsersRepository.getInstance().changePassword(onUserChangePassword, oldPassword, newPassword);
    }
}
