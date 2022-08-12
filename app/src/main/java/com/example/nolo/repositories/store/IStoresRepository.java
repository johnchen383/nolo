package com.example.nolo.repositories.store;

import com.example.nolo.entities.store.IStore;

import java.util.function.Consumer;

public interface IStoresRepository {
    void loadStores(Consumer<Class<?>> loadedRepository);
    IStore getStoreById(String storeId);
}
