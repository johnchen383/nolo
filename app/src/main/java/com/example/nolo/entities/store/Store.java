package com.example.nolo.entities.store;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

import java.util.List;

public class Store implements IStore {
    /**
      * Object list cannot use IBranch (Branch Interface),
      * the reason is when the Firebase auto converts the data into
      * the object, it is unable to deserialize the object.
      * It is because the interface does not have 0-argument constructor.
      * To have the Firebase auto converts the data into the object,
      * our team decided to use Branch class as the object list.
      * So it is a reasonable excuse to violate the SOLID principle.
      */
    private List<Branch> branches;
    private String storeId, storeName;

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public Store() {}

    public Store(String storeName, List<Branch> branches) {
        this.storeName = storeName;
        this.branches = branches;
    }

    @Override
    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    @Override
    @Exclude
    public String getStoreId() {
        return storeId;
    }

    @Override
    public String getStoreName() {
        return storeName;
    }

    @Override
    public List<Branch> getBranches() {
        return branches;
    }

    @NonNull
    @Override
    public String toString() {
        return "Store{" +
                "storeId='" + storeId + '\'' +
                ", storeName='" + storeName + '\'' +
                ", branches=" + branches +
                '}';
    }
}
