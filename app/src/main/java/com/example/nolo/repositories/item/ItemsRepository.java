package com.example.nolo.repositories.item;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This is a singleton class for Items repository.
 */
public class ItemsRepository implements IItemsRepository {
    private static ItemsRepository itemsRepository = null;
    private final FirebaseFirestore db;

    private ItemsRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public static ItemsRepository getInstance() {
        if (itemsRepository == null)
            itemsRepository = new ItemsRepository();

        return itemsRepository;
    }
}
