package com.example.nolo.repositories.item;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.nolo.entities.item.Accessory;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.entities.item.Laptop;
import com.example.nolo.entities.item.Phone;
import com.example.nolo.entities.item.variant.ItemVariant;
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
import java.util.function.Consumer;

/**
 * This is a singleton class for Items repository.
 */
public class ItemsRepository implements IItemsRepository {
    private static ItemsRepository itemsRepository = null;
    private final FirebaseFirestore db;
    private final TimeToLiveToken timeToLiveToken;
    private final Set<CategoryType> loadableCategoryItems, loadedCategoryItems;
    private List<IItem> allItemsRepo, laptopsRepo, phonesRepo, accessoriesRepo;

    private ItemsRepository() {
        db = FirebaseFirestore.getInstance();
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
     * Get the collection path in String by category type
     */
    private String getCollectionPathByCategoryType(CategoryType categoryType) {
        switch (categoryType) {
            case laptops:
                return CollectionPath.laptops.name();
            case phones:
                return CollectionPath.phones.name();
            case accessories:
                return CollectionPath.accessories.name();
            default:
                Log.e("ItemsRepository", "This shouldn't happen! Occur in getCollectionPathByCategoryType() in " + getClass().getSimpleName());
                return CollectionPath.laptops.name();
        }
    }

    /**
     * Convert the document from Firebase into Item, specify the item to convert to by category type
     */
    private IItem convertToItem(CategoryType categoryType, QueryDocumentSnapshot document) {
        switch (categoryType) {
            case laptops:
                return document.toObject(Laptop.class);
            case phones:
                return document.toObject(Phone.class);
            case accessories:
                return document.toObject(Accessory.class);
            default:
                Log.e("ItemsRepository", "This shouldn't happen! Occur in loadRepo() in " + getClass().getSimpleName());
                return document.toObject(Laptop.class);
        }
    }

    /**
     * Store the repo into a specific repository, specify the repository to store to by category type
     */
    private void storeRepoByCategoryType(CategoryType categoryType, List<IItem> repo) {
        switch (categoryType) {
            case laptops:
                laptopsRepo = repo;
                break;
            case phones:
                phonesRepo = repo;
                break;
            case accessories:
                accessoriesRepo = repo;
                break;
            default:
                Log.e("ItemsRepository", "This shouldn't happen! Occur in storeRepoByCategoryType() in " + getClass().getSimpleName());
                break;
        }
    }

    /**
     * Load Item from Firebase
     */
    private void loadRepo(Consumer<Class<?>> onLoadedRepository, CategoryType categoryType) {
        List<IItem> repo = new ArrayList<>();
        String collectionPath = getCollectionPathByCategoryType(categoryType);

        db.collection(collectionPath).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        IItem item = convertToItem(categoryType, document);
                        item.setCategoryType(categoryType);
                        item.setItemId(document.getId());  // store document ID after getting the object
                        repo.add(item);

                        Log.i("Load " + collectionPath + " From Firebase", item.toString());
                    }

                    if (repo.size() > 0) {
                        Log.i("Load " + collectionPath + " From Firebase", "Success");
                    } else {
                        Log.i("Load " + collectionPath + " From Firebase", collectionPath + " collection is empty!");
                    }
                } else {
                    Log.i("Load " + collectionPath + " From Firebase", "Loading " + collectionPath + " collection failed from Firestore!");
                }

                storeRepoByCategoryType(categoryType, repo);

                // inform this accessories finished loading
                onLoadItemsRepoCacheComplete(onLoadedRepository, categoryType);
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
        allItemsRepo = new ArrayList<>();
        loadedCategoryItems.clear();
        timeToLiveToken.reset();

        loadRepo(onLoadedRepository, CategoryType.laptops);
        loadRepo(onLoadedRepository, CategoryType.phones);
        loadRepo(onLoadedRepository, CategoryType.accessories);
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
}
