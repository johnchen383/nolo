package com.example.nolo.dataprovider;

import static com.example.nolo.repositories.store.StoresRepository.COLLECTION_PATH_STORES;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.nolo.entities.store.Branch;
import com.example.nolo.entities.store.IStore;
import com.example.nolo.entities.store.Store;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class DataProvider {
    private static List<IStore> generateStores() {
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

    public static void addStoresToFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<IStore> stores = generateStores();

        for (IStore store : stores) {
            db.collection(COLLECTION_PATH_STORES).add(store).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.i("Add stores to Firebase", store.getStoreId() + " added.");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("Add stores to Firebase", store.getStoreId() + " NOT added.");
                }
            });
        }
    }
}
