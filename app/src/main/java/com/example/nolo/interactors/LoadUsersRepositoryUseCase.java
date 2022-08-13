package com.example.nolo.interactors;

import com.example.nolo.repositories.user.UsersRepository;

import java.util.function.Consumer;

public class LoadUsersRepositoryUseCase {
    public static void loadStoresRepository(Consumer<Class<?>> loadedRepository) {
        UsersRepository.getInstance().loadUsers(loadedRepository);
    }
}