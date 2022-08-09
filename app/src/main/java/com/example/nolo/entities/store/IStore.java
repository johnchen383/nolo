package com.example.nolo.entities.store;

import java.util.List;

public interface IStore {
    String getStoreId();
    String getStoreName();
    List<IBranch> getBranches();
}
