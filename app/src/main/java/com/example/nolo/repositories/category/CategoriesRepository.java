package com.example.nolo.repositories.category;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.nolo.entities.category.Category;
import com.example.nolo.entities.category.ICategory;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.enums.CollectionPath;
import com.example.nolo.repositories.RepositoryExpiredTime;
import com.example.nolo.util.TimeKeeper;
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
    private static CategoriesRepository categoriesRepository = null;
    private final FirebaseFirestore db;
    private final List<ICategory> categoriesRepo;
    private final TimeKeeper timerForCache;

    private CategoriesRepository() {
        db = FirebaseFirestore.getInstance();
        categoriesRepo = new ArrayList<>();
        timerForCache = new TimeKeeper(RepositoryExpiredTime.TIME_LIMIT);
    }

    /**
     * This is for singleton class.
     */
    public static CategoriesRepository getInstance() {
        if (categoriesRepository == null)
            categoriesRepository = new CategoriesRepository();

        return categoriesRepository;
    }

    /**
     * Reload data from Firebase if the cached data is outdated/expired.
     */
    private void reloadCategoriesIfExpired() {
        if (timerForCache.isTimeLimitReached())
            loadCategories(a -> {});
    }

    /**
     * Load data from Firebase.
     */
    @Override
    public void loadCategories(Consumer<Class<?>> onLoadedRepository) {
        categoriesRepo.clear();
        timerForCache.startTimer();

        db.collection(CollectionPath.categories.name()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ICategory category = document.toObject(Category.class);
                        category.setCategoryType(CategoryType.valueOf(document.getId()));  // store document ID after getting the object
                        categoriesRepo.add(category);
                        Log.i("Load Categories From Firebase", category.toString());
                    }

                    if (categoriesRepo.size() > 0) {
                        Log.i("Load Categories From Firebase", "Success");
                    } else {
                        Log.i("Load Categories From Firebase", "Categories collection is empty!");
                    }
                } else {
                    Log.i("Load Categories From Firebase", "Loading Categories collection failed from Firestore!");
                }

                // inform this repository finished loading
                onLoadedRepository.accept(CategoriesRepository.class);
            }
        });
    }

    /**
     * Get Category entity by Category Type enum
     *
     * @param categoryType Specific Category Type in enum
     * @return Category entity if categoryType exists;
     *         Otherwise null
     */
    @Override
    public ICategory getCategoryByType(CategoryType categoryType) {
        ICategory result = null;
        for (ICategory category : categoriesRepo) {
            if (category.getCategoryType().equals(categoryType)) {
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

        return categoriesRepo;
    }


}
