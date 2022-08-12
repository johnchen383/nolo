package com.example.nolo.repositories.category;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This is a singleton class for Categories repository.
 */
public class CategoriesRepository implements ICategoriesRepository {
    public static final String COLLECTION_PATH_CATEGORIES = "categories";

    private static CategoriesRepository categoriesRepository = null;
    private final FirebaseFirestore db;

    private CategoriesRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public static CategoriesRepository getInstance() {
        if (categoriesRepository == null)
            categoriesRepository = new CategoriesRepository();

        return categoriesRepository;
    }
}
