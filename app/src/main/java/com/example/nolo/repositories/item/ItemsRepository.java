package com.example.nolo.repositories.item;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.nolo.entities.item.Accessory;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.entities.item.Laptop;
import com.example.nolo.entities.item.Phone;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.enums.CollectionPath;
import com.example.nolo.repositories.RepositoryExpiredTime;
import com.example.nolo.util.TimeToLiveToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * This is a singleton class for Items repository.
 */
public class ItemsRepository implements IItemsRepository {
    private static ItemsRepository itemsRepository = null;
    private final FirebaseFirestore db;
    private final List<IItem> allItemsRepo, laptopsRepo, phonesRepo, accessoriesRepo;
    private final TimeToLiveToken timeToLiveToken;
    private final Set<CategoryType> loadableCategoryItems, loadedCategoryItems;

    private ItemsRepository() {
        db = FirebaseFirestore.getInstance();
        allItemsRepo = new ArrayList<>();
        laptopsRepo = new ArrayList<>();
        phonesRepo = new ArrayList<>();
        accessoriesRepo = new ArrayList<>();
        timeToLiveToken = new TimeToLiveToken(RepositoryExpiredTime.TIME_LIMIT);
        loadableCategoryItems = new HashSet<>(Arrays.asList(
                CategoryType.laptops,
                CategoryType.phones,
                CategoryType.accessories
        ));
        loadedCategoryItems = new HashSet<>();
    }

    /**
     * This is for singleton class.
     */
    public static ItemsRepository getInstance() {
        if (itemsRepository == null)
            itemsRepository = new ItemsRepository();

        return itemsRepository;
    }

    /**
     * Reload data from Firebase if the cached data is outdated/expired.
     */
    private void reloadItemsIfExpired() {
        if (timeToLiveToken.hasExpired())
            loadItems(a -> {});
    }

    /**
     * Load Laptop from Firebase.
     */
    private void loadLaptopsRepo(Consumer<Class<?>> onLoadedRepository, BiConsumer<Consumer<Class<?>>, CategoryType> onLoadedLaptopsRepo) {
        laptopsRepo.clear();

        db.collection(CollectionPath.laptops.name()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        IItem item = document.toObject(Laptop.class);
                        item.setItemId(document.getId());  // store document ID after getting the object
                        laptopsRepo.add(item);

                        Log.i("Load Laptops From Firebase", item.toString());
                    }

                    if (laptopsRepo.size() > 0) {
                        Log.i("Load Laptops From Firebase", "Success");
                    } else {
                        Log.i("Load Laptops From Firebase", "Laptops collection is empty!");
                    }
                } else {
                    Log.i("Load Laptops From Firebase", "Loading Laptops collection failed from Firestore!");
                }

                // inform laptops repository finished loading
                onLoadedLaptopsRepo.accept(onLoadedRepository, CategoryType.laptops);
            }
        });
    }

    /**
     * Load Phone from Firebase.
     */
    private void loadPhonesRepo(Consumer<Class<?>> onLoadedRepository, BiConsumer<Consumer<Class<?>>, CategoryType> onLoadedPhonesRepo) {
        phonesRepo.clear();

        db.collection(CollectionPath.phones.name()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        IItem item = document.toObject(Phone.class);
                        item.setItemId(document.getId());  // store document ID after getting the object
                        phonesRepo.add(item);

                        Log.i("Load Phones From Firebase", item.toString());
                    }

                    if (phonesRepo.size() > 0) {
                        Log.i("Load Phones From Firebase", "Success");
                    } else {
                        Log.i("Load Phones From Firebase", "Phones collection is empty!");
                    }
                } else {
                    Log.i("Load Phones From Firebase", "Loading Phones collection failed from Firestore!");
                }

                // inform phones repository finished loading
                onLoadedPhonesRepo.accept(onLoadedRepository, CategoryType.phones);
            }
        });
    }

    /**
     * Load Accessory from Firebase.
     */
    private void loadAccessoriesRepo(Consumer<Class<?>> onLoadedRepository, BiConsumer<Consumer<Class<?>>, CategoryType> onLoadedAccessoriesRepo) {
        accessoriesRepo.clear();

        db.collection(CollectionPath.accessories.name()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        IItem item = document.toObject(Accessory.class);
                        item.setItemId(document.getId());  // store document ID after getting the object
                        accessoriesRepo.add(item);

                        Log.i("Load Accessories From Firebase", item.toString());
                    }

                    if (accessoriesRepo.size() > 0) {
                        Log.i("Load Accessories From Firebase", "Success");
                    } else {
                        Log.i("Load Accessories From Firebase", "Accessories collection is empty!");
                    }
                } else {
                    Log.i("Load Accessories From Firebase", "Loading Accessories collection failed from Firestore!");
                }

                // inform this accessories finished loading
                onLoadedAccessoriesRepo.accept(onLoadedRepository, CategoryType.accessories);
            }
        });
    }

    /**
     * When all items repository are loaded, inform that it is done
     */
    private void onLoadItemsRepoCacheComplete(Consumer<Class<?>> onLoadedRepository, CategoryType itemCategory) {
        loadedCategoryItems.add(itemCategory);

        if (loadedCategoryItems.equals(loadableCategoryItems)) {
            Log.i("Load Items From Firebase", "Success");

            allItemsRepo.addAll(laptopsRepo);
            allItemsRepo.addAll(phonesRepo);
            allItemsRepo.addAll(accessoriesRepo);

            // inform Items repository finished loading
            onLoadedRepository.accept(ItemsRepository.class);
        }
    }

    /**
     * Load data from Firebase.
     */
    @Override
    public void loadItems(Consumer<Class<?>> onLoadedRepository) {
        allItemsRepo.clear();
        loadedCategoryItems.clear();
        timeToLiveToken.reset();

        loadLaptopsRepo(onLoadedRepository, this::onLoadItemsRepoCacheComplete);
        loadPhonesRepo(onLoadedRepository, this::onLoadItemsRepoCacheComplete);
        loadAccessoriesRepo(onLoadedRepository, this::onLoadItemsRepoCacheComplete);
    }

    @Override
    public List<IItem> getAllItems() {
        reloadItemsIfExpired();

        return allItemsRepo;
    }

    /**
     * Get Item entity by item ID
     *
     * @param itemId item ID
     * @return Item entity if itemId exists;
     *         Otherwise null
     */
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

    /**
     * Get list of Items by list of item IDs.
     * Convert list of Item IDs to list of Item entities
     *
     * @param itemIds List of item IDs
     * @return List of Item entities
     */
    @Override
    public List<IItem> getItemByIdList(List<String> itemIds) {
        List<IItem> result = new ArrayList<>();

        for (IItem item : allItemsRepo) {
            if (itemIds.contains(item.getItemId())) {
                result.add(item);
            }
        }

        reloadItemsIfExpired();
        return result;
    }

    /**
     * Get list of Items by a specific Category Type
     *
     * @param categoryType Specific Category Type
     * @return List of Items if categoryType exists;
     *         Otherwise empty list
     */
    @Override
    public List<IItem> getCategoryItems(CategoryType categoryType) {
        List<IItem> result = new ArrayList<>();

        switch (categoryType) {
            case laptops:
                result = laptopsRepo;
                break;
            case phones:
                result = phonesRepo;
                break;
            case accessories:
                result = accessoriesRepo;
                break;
        }

        reloadItemsIfExpired();
        return result;
    }

    /**
     * Get list of Items by the search terms
     *
     * @param searchTerm Search terms
     * @return List of Items if Items' names contain search term;
     *         Otherwise empty list
     */
    @Override
    public List<IItem> getSearchSuggestions(String searchTerm) {
        List<IItem> result = new ArrayList<>();
        for (IItem item : allItemsRepo) {
            if (item.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                result.add(item);
            }
        }

        reloadItemsIfExpired();
        return result;
    }

    /**
     * Get list of Accessory entities that are recommended of a specific Item
     *
     * @param itemId Specific Item ID
     * @return List of Accessory entities
     */
    @Override
    public List<IItem> getAccessRecommendationsByItemId(String itemId) {
        return getItemByIdList(getItemById(itemId).getRecommendedAccessoryIds());
    }
}
