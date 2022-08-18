package com.example.nolo.interactors.user;

import com.example.nolo.entities.user.IUser;
import com.example.nolo.repositories.user.UsersRepository;

public class GetCurrentUserUseCase {
    /**
     * This method return the current user in User entity if the user is signed in,
     * if the user is not signed in, it will return null.
     *
     * @return Current user if signed in;
     *         Otherwise null
     */
    public static IUser getCurrentUser() {
        return UsersRepository.getInstance().getCurrentUser();
    }
}
