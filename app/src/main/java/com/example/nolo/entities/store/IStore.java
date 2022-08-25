package com.example.nolo.entities.store;

import java.util.List;

public interface IStore {
    String getStoreId();
    void setStoreId(String storeId);
    String getStoreName();
    List<Branch> getBranches();
}
