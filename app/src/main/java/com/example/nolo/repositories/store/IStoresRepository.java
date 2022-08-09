package com.example.nolo.repositories.store;

import com.example.nolo.entities.store.Store;

public interface IStoresRepository {
    Store fetchStoreById(String storeId);
}
