package com.example.nolo.repositories;

import com.example.nolo.entities.store.Store;

public interface IStoresRepository {
    Store fetchStoreById(String storeId);
}
