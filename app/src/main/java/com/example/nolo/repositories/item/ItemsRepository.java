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
    private final List<IItem> itemsRepo;
    private long lastLoadedTime;

    private ItemsRepository() {
        db = FirebaseFirestore.getInstance();
        itemsRepo = new ArrayList<>();
        lastLoadedTime = 0;
    }

    public static ItemsRepository getInstance() {
        if (itemsRepository == null)
            itemsRepository = new ItemsRepository();

        return itemsRepository;
    }

    @Override
    public void loadItems(Consumer<Class<?>> loadedRepository) {

    }

    @Override
    public List<IItem> getAllItems() {
        return null;
    }

    @Override
    public IItem getItemById(String itemId) {
        return null;
    }
}
