package com.example.nolo.interactors.user;

import com.example.nolo.repositories.user.UsersRepository;

import java.util.function.Consumer;

public class LoadUsersRepositoryUseCase {
    /**
     *
     * @param loadedRepository
     */
    public static void loadUsersRepository(Consumer<Class<?>> loadedRepository) {
        UsersRepository.getInstance().loadUsers(loadedRepository);
    }
}
