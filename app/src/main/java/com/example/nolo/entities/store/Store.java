package com.example.nolo.entities.store;

import java.util.List;

public class Store implements IStore {
    private String storeId, storeName;
    private List<IBranch> branches;

    public Store() {}

    public Store(String storeId, String storeName, List<IBranch> branches) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.branches = branches;
    }

    @Override
    public String getStoreId() {
        return storeId;
    }

    @Override
    public String getStoreName() {
        return storeName;
    }

    @Override
    public List<IBranch> getBranches() {
        return branches;
    }
}
