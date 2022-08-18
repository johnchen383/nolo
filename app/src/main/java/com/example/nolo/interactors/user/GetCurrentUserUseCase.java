package com.example.nolo.interactors.user;

import com.example.nolo.entities.user.IUser;
import com.example.nolo.repositories.user.UsersRepository;

public class GetCurrentUserUseCase {
    /**
     *
     * @return
     */
    public static IUser getCurrentUser() {
        return UsersRepository.getInstance().getCurrentUser();
    }
}
