package com.example.nolo.repositories.item;

import com.example.nolo.entities.item.IItem;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * This is a singleton class for Items repository.
 */
public class ItemsRepository implements IItemsRepository {
    public static final long TIME_IN_MILLISECONDS_TEN_MINUTES = 1000*60*10;
    public static final String COLLECTION_PATH_LAPTOPS = "laptops";
    public static final String COLLECTION_PATH_PHONES = "phones";
    public static final String COLLECTION_PATH_ACCESSORIES = "accessories";

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
        if (System.currentTimeMillis() - lastLoadedTime > TIME_IN_MILLISECONDS_TEN_MINUTES)
            loadItems(a -> {});
    }

    /*
     * Load data from Firebase.
     */
    @Override
    public void loadItems(Consumer<Class<?>> loadedRepository) {
        allItemsRepo.clear();
        lastLoadedTime = System.currentTimeMillis();


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
