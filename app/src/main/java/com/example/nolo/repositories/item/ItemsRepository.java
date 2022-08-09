package com.example.nolo.repositories.item;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This is a singleton class for Items repository.
 */
public class ItemsRepository implements IItemsRepository {
    private static ItemsRepository itemsRepository = null;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ItemsRepository() {}

    public static ItemsRepository getInstance() {
        if (itemsRepository == null)
            itemsRepository = new ItemsRepository();

        return itemsRepository;
    }
}
