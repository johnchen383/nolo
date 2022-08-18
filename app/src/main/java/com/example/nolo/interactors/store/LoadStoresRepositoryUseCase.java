package com.example.nolo.interactors.store;

import com.example.nolo.repositories.store.StoresRepository;

import java.util.function.Consumer;

public class LoadStoresRepositoryUseCase {
    /**
     * Load data from Firebase
     *
     * @param onLoadedRepository A function that will run after the repository is loaded
     */
    public static void loadStoresRepository(Consumer<Class<?>> onLoadedRepository) {
        StoresRepository.getInstance().loadStores(onLoadedRepository);
    }
}
