package com.example.nolo.dataprovider;

import com.example.nolo.entities.store.Branch;
import com.example.nolo.entities.store.IStore;
import com.example.nolo.entities.store.Store;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DataProvider {
    public static List<IStore> generateStores() {
        List<IStore> stores = new ArrayList<>();

        List<Branch> branches = new ArrayList<>();
        branches.add(new Branch("botany", "(2, 3)"));
        branches.add(new Branch("pakang", "(3, 2)"));
        stores.add(new Store("asd", branches));

        branches = new ArrayList<>();
        branches.add(new Branch("asb", "(9, 3)"));
        stores.add(new Store("jbhifi", branches));

        return stores;
    }

    public static void addStoreToFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<IStore> stores = generateStores();
        for (IStore store : stores)
            db.collection("stores").add(store);
    }
}
