package com.example.nolo.dataprovider;

import com.example.nolo.entities.store.Branch;
import com.example.nolo.entities.store.IStore;
import com.example.nolo.entities.store.Store;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class DataProvider {
    public static List<IStore> generateStores() {
        List<IStore> stores = new ArrayList<>();

        List<Branch> branches = new ArrayList<>();
        branches.add(new Branch("botany", "17 Botany Street", new GeoPoint(3, 5)));
        branches.add(new Branch("pakang", "51 Pak Lane", new GeoPoint(10, 8)));
        stores.add(new Store("pbtech", branches));

        branches = new ArrayList<>();
        branches.add(new Branch("manuka", "9 Manuka Ave", new GeoPoint(-9, -15)));
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
