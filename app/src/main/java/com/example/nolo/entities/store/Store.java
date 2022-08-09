package com.example.nolo.entities.store;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

import java.util.List;

public class Store implements IStore {
    private String storeId, storeName;
    private List<Branch> branches;

    public Store() {}

    public Store(String storeName, List<Branch> branches) {
        this.storeName = storeName;
        this.branches = branches;
    }

    @Override
    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    @Override
    @Exclude
    public String getStoreId() {
        return storeId;
    }

    @Override
    public String getStoreName() {
        return storeName;
    }

    @Override
    public List<Branch> getBranches() {
        return branches;
    }

    @NonNull
    @Override
    public String toString() {
        String result = "Store ID: " + storeId + "; Store name: " + storeName + "; Branch names: ";
        for (IBranch branch : branches) {
            result += branch.getBranchName() + ", ";
        }
        return result;
    }
}
