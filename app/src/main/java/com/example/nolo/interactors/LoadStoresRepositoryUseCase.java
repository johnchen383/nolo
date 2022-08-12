package com.example.nolo.interactors;

import com.example.nolo.repositories.store.StoresRepository;

import java.util.function.Consumer;

public class LoadStoresRepositoryUseCase {
    public static void loadStoresRepository(Consumer<Class<?>> loadedRepository) {
        StoresRepository.getInstance().loadStores(loadedRepository);
    }
}
