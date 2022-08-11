package com.example.nolo.interactors;

import com.example.nolo.repositories.store.StoresRepository;

import java.util.function.Consumer;

public class LoadAllRepositoriesUseCase {
    public static void loadAllRepository(Consumer<Class<?>> loadedRepository) {
        StoresRepository.getInstance().loadStores(loadedRepository);
    }
}
