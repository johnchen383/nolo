package com.example.nolo.interactors.store;

import com.example.nolo.repositories.store.StoresRepository;

import java.util.function.Consumer;

public class LoadStoresRepositoryUseCase {
    /**
     * Load data from Firebase
     *
     * @param loadedRepository A function that will run after the repository is loaded
     */
    public static void loadStoresRepository(Consumer<Class<?>> loadedRepository) {
        StoresRepository.getInstance().loadStores(loadedRepository);
    }
}
