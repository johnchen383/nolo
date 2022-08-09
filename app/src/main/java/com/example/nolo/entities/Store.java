package com.example.nolo.entities;

public class Store implements IStore {
    private String storeId, storeName;

    public Store() {}

    public Store(String storeId, String storeName) {
        this.storeId = storeId;
        this.storeName = storeName;
    }

    @Override
    public String getStoreId() {
        return storeId;
    }

    @Override
    public String getStoreName() {
        return storeName;
    }
}
