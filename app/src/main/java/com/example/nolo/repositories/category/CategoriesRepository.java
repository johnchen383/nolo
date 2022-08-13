package com.example.nolo.repositories.category;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.nolo.entities.category.Category;
import com.example.nolo.entities.category.ICategory;
import com.example.nolo.entities.store.IStore;
import com.example.nolo.entities.store.Store;
import com.example.nolo.repositories.store.StoresRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * This is a singleton class for Categories repository.
 */
public class CategoriesRepository implements ICategoriesRepository {
    public static final long TIME_IN_MILLISECONDS_TEN_MINUTES = 1000*60*10;
    public static final String COLLECTION_PATH_CATEGORIES = "categories";

    private static CategoriesRepository categoriesRepository = null;
    private final FirebaseFirestore db;
    private final List<ICategory> categories;
    private long lastLoadedTime;

    private CategoriesRepository() {
        db = FirebaseFirestore.getInstance();
        categories = new ArrayList<>();
        lastLoadedTime = 0;
    }

    /*
     * This is for singleton class.
     */
    public static CategoriesRepository getInstance() {
        if (categoriesRepository == null)
            categoriesRepository = new CategoriesRepository();

        return categoriesRepository;
    }

    /*
     * Reload data from Firebase if the cached data is outdated/expired.
     */
    private void reloadCategoriesIfExpired() {
        if (System.currentTimeMillis() - lastLoadedTime > TIME_IN_MILLISECONDS_TEN_MINUTES)
            loadCategories(a -> {});
    }

    /*
     * Load data from Firebase.
     */
    @Override
    public void loadCategories(Consumer<Class<?>> loadedRepository) {
        categories.clear();
        lastLoadedTime = System.currentTimeMillis();

        db.collection(COLLECTION_PATH_CATEGORIES).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ICategory category = document.toObject(Category.class);
                        category.setCategoryId(document.getId());  // store document ID after getting the object
                        categories.add(category);
                        Log.i("Load Categories From Firebase", category.toString());
                    }

                    if (categories.size() > 0) {
                        Log.i("Load Categories From Firebase", "Success");
                    } else {
                        Log.i("Load Categories From Firebase", "Categories collection is empty!");
                    }
                } else {
                    Log.i("Load Categories From Firebase", "Loading Categories collection failed from Firestore!");
                }

                // inform this repository finished loading
                loadedRepository.accept(CategoriesRepository.class);
            }
        });
    }

    @Override
    public ICategory getCategoryById(String categoryId) {
        ICategory result = null;
        for (ICategory category : categories) {
            if (category.getCategoryId().equals(categoryId)) {
                result = category;
                break;
            }
        }

        reloadCategoriesIfExpired();
        return result;
    }

    @Override
    public List<ICategory> getCategories() {
        reloadCategoriesIfExpired();

        return categories;
    }


}
