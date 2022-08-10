package com.example.nolo.repositories.store;

import com.example.nolo.entities.store.IStore;

import java.util.List;
import java.util.function.Consumer;

public interface IStoresRepository {
    void loadStores(Consumer<List<IStore>> function);
    IStore getStoreById(String storeId);
}
