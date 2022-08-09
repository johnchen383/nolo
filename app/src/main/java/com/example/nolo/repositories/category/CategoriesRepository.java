package com.example.nolo.repositories.category;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This is a singleton class for Categories repository.
 */
public class CategoriesRepository implements ICategoriesRepository {
    private static CategoriesRepository categoriesRepository = null;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CategoriesRepository() {}

    public static CategoriesRepository getInstance() {
        if (categoriesRepository == null)
            categoriesRepository = new CategoriesRepository();

        return categoriesRepository;
    }
}
