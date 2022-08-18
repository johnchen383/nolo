package com.example.nolo.interactors.user;

import com.example.nolo.repositories.user.UsersRepository;

import java.util.function.Consumer;

public class LoadUsersRepositoryUseCase {
    /**
     * Load data from Firebase
     *
     * @param onLoadedRepository A function that will run after the repository is loaded
     */
    public static void loadUsersRepository(Consumer<Class<?>> onLoadedRepository) {
        UsersRepository.getInstance().loadUsers(onLoadedRepository);
    }
}
