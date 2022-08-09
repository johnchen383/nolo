package com.example.nolo.repositories.store;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.nolo.entities.store.IStore;
import com.example.nolo.entities.store.Store;
import com.example.nolo.repositories.Collections;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
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
        db.collection(Collections.STORES).document(storeId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    IStore store = task.getResult().toObject(Store.class);
                    if (store != null) {
                        store.setStoreId(task.getResult().getId());
                        Log.i("Fetching Store By ID", store.toString());
                    }
                } else {
                    Log.i("Fetching Store By ID", "Loading stores collection failed from Firestore!");
                }
            }
        });
        return null;
    }
}
