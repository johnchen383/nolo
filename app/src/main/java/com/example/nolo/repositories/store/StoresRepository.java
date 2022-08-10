package com.example.nolo.repositories.store;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.nolo.entities.store.IStore;
import com.example.nolo.entities.store.Store;
import com.example.nolo.repositories.CollectionPath;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
  * This is a singleton class for Stores repository.
  */
public class StoresRepository implements IStoresRepository {
    private static StoresRepository storesRepository = null;
    private FirebaseFirestore db;
    private List<IStore> stores;

    private StoresRepository() {
        db = FirebaseFirestore.getInstance();
        stores = new ArrayList<>();
    }

    public static StoresRepository getInstance() {
        if (storesRepository == null)
            storesRepository = new StoresRepository();

        return storesRepository;
    }

    @Override
    public void loadStores(Consumer<List<IStore>> function) {
        db.collection(CollectionPath.STORES).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        IStore store = document.toObject(Store.class);
                        store.setStoreId(document.getId());
                        stores.add(store);
                        Log.i("Load Stores From Firebase", store.toString());
                    }
                    function.accept(stores);

                    if (stores.size() > 0) {
                        Log.i("Load Stores From Firebase", "Success");
                    } else {
                        Log.i("Load Stores From Firebase", "Stores collection is empty!");
                    }
                } else {
                    Log.i("Load Stores From Firebase", "Loading Stores collection failed from Firestore!");
                }
            }
        });
    }

    @Override
    public IStore getStoreById(String storeId) {
        for (IStore store : stores) {
            if (store.getStoreId().equals(storeId)) {
                return store;
            }
        }

        return null;
    }
}
