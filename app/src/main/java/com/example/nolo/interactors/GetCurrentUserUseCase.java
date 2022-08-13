package com.example.nolo.interactors;

import com.example.nolo.entities.user.IUser;
import com.example.nolo.repositories.user.UsersRepository;

public class GetCurrentUserUseCase {
    public static IUser getCurrentUser() {
        return UsersRepository.getInstance().getCurrentUser();
    }
}
