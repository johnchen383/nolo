package com.example.nolo.entities.store;

import java.util.List;

public interface IStore {
    void setStoreId(String storeId);
    String getStoreId();
    String getStoreName();
    List<Branch> getBranches();
}
