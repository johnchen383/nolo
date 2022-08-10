package com.example.nolo.repositories.store;

import com.example.nolo.entities.store.IStore;

public interface IStoresRepository {
    IStore fetchStoreById(String storeId);
}
