package com.example.nolo.repositories.store;

import com.example.nolo.entities.store.Store;
import com.google.firebase.firestore.FirebaseFirestore;

public class StoresRepository implements IStoresRepository {
    private static StoresRepository storesRepository = null;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private StoresRepository() {}

    public static StoresRepository getInstance() {
        if (storesRepository == null)
            storesRepository = new StoresRepository();

        return storesRepository;
    }

    @Override
    public Store fetchStoreById(String storeId) {
        return null;
    }
}
