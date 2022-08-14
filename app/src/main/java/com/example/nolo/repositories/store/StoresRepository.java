package com.example.nolo.repositories.store;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.nolo.entities.store.IStore;
import com.example.nolo.entities.store.Store;
import com.example.nolo.repositories.CollectionPath;
import com.example.nolo.repositories.RepositoryExpiredTime;
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
    private final FirebaseFirestore db;
    private final List<IStore> storesRepo;
    private long lastLoadedTime;

    private StoresRepository() {
        db = FirebaseFirestore.getInstance();
        storesRepo = new ArrayList<>();
        lastLoadedTime = 0;
    }

    /*
     * This is for singleton class.
     */
    public static StoresRepository getInstance() {
        if (storesRepository == null)
            storesRepository = new StoresRepository();

        return storesRepository;
    }

    /*
     * Reload data from Firebase if the cached data is outdated/expired.
     */
    private void reloadStoresIfExpired() {
        if (System.currentTimeMillis() - lastLoadedTime > RepositoryExpiredTime.TIME_LIMIT)
            loadStores(a -> {});
    }

    /*
     * Load data from Firebase.
     */
    @Override
    public void loadStores(Consumer<Class<?>> loadedRepository) {
        storesRepo.clear();
        lastLoadedTime = System.currentTimeMillis();

        db.collection(CollectionPath.stores.name()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        IStore store = document.toObject(Store.class);
                        store.setStoreId(document.getId());  // store document ID after getting the object
                        storesRepo.add(store);
                        Log.i("Load Stores From Firebase", store.toString());
                    }

                    if (storesRepo.size() > 0) {
                        Log.i("Load Stores From Firebase", "Success");
                    } else {
                        Log.i("Load Stores From Firebase", "Stores collection is empty!");
                    }
                } else {
                    Log.i("Load Stores From Firebase", "Loading Stores collection failed from Firestore!");
                }

                // inform this repository finished loading
                loadedRepository.accept(StoresRepository.class);
            }
        });
    }

    @Override
    public IStore getStoreById(String storeId) {
        IStore result = null;
        for (IStore store : storesRepo) {
            if (store.getStoreId().equals(storeId)) {
                result = store;
                break;
            }
        }

        reloadStoresIfExpired();
        return result;
    }
}
