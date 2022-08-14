package com.example.nolo.repositories.item;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.nolo.entities.item.IItem;
import com.example.nolo.entities.item.Laptop;
import com.example.nolo.repositories.CollectionPath;
import com.example.nolo.repositories.RepositoryExpiredTime;
import com.example.nolo.repositories.category.CategoriesRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * This is a singleton class for Items repository.
 */
public class ItemsRepository implements IItemsRepository {
    private static ItemsRepository itemsRepository = null;
    private final FirebaseFirestore db;
    private final List<IItem> allItemsRepo, laptopsRepo, phonesRepo, accessoriesRepo;
    private long lastLoadedTime;

    private ItemsRepository() {
        db = FirebaseFirestore.getInstance();
        allItemsRepo = new ArrayList<>();
        laptopsRepo = new ArrayList<>();
        phonesRepo = new ArrayList<>();
        accessoriesRepo = new ArrayList<>();
        lastLoadedTime = 0;
    }

    public static ItemsRepository getInstance() {
        if (itemsRepository == null)
            itemsRepository = new ItemsRepository();

        return itemsRepository;
    }

    /*
     * Reload data from Firebase if the cached data is outdated/expired.
     */
    private void reloadItemsIfExpired() {
        if (System.currentTimeMillis() - lastLoadedTime > RepositoryExpiredTime.TIME_LIMIT)
            loadItems(a -> {});
    }

    /*
     * Load data from Firebase.
     */
    @Override
    public void loadItems(Consumer<Class<?>> loadedRepository) {
        allItemsRepo.clear();
        laptopsRepo.clear();
        phonesRepo.clear();
        accessoriesRepo.clear();
        lastLoadedTime = System.currentTimeMillis();

        db.collection(CollectionPath.laptops.name()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        IItem item = document.toObject(Laptop.class);
                        item.setItemId(document.getId());  // store document ID after getting the object
                        laptopsRepo.add(item);
                        allItemsRepo.add(item);

                        Log.i("Load Laptops From Firebase", item.toString());
                    }

                    if (allItemsRepo.size() > 0) {
                        Log.i("Load Laptops From Firebase", "Success");
                    } else {
                        Log.i("Load Laptops From Firebase", "Laptops collection is empty!");
                    }
                } else {
                    Log.i("Load Laptops From Firebase", "Loading Laptops collection failed from Firestore!");
                }

                // inform this repository finished loading
                loadedRepository.accept(CategoriesRepository.class);  // TODO: how to inform it when try to retrieve 3 collections?
            }
        });
    }

    @Override
    public List<IItem> getAllItems() {
        reloadItemsIfExpired();

        return allItemsRepo;
    }

    @Override
    public IItem getItemById(String itemId) {
        IItem result = null;
        for (IItem item : allItemsRepo) {
            if (item.getItemId().equals(itemId)) {
                result = item;
                break;
            }
        }

        reloadItemsIfExpired();
        return result;
    }
}
