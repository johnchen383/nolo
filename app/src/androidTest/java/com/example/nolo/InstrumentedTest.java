package com.example.nolo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.nolo.entities.store.Branch;
import com.example.nolo.entities.store.IStore;
import com.example.nolo.entities.store.Store;
import com.example.nolo.repositories.Collections;
import com.example.nolo.repositories.store.StoresRepository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {

    /**
     * Fetch store by id where store exists in Firebase
     */
    @Test
    public void testFetchStoreById_OneStoreInFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<Branch> branches = new ArrayList<>();

        branches.add(new Branch("botany", "17 Botany Street", new GeoPoint(3, 5)));
        branches.add(new Branch("pakang", "51 Pak Lane", new GeoPoint(10, 8)));
        IStore testStore = new Store("pbtech", branches);

        db.collection(Collections.STORES).add(testStore).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String id = documentReference.getId();
                IStore store = StoresRepository.getInstance().fetchStoreById(id);
                assertEquals(store, testStore);
            }
        });
    }

    /**
     * Fetch store by id where store does not exist in Firebase
     */
    @Test
    public void testFetchStoreById_NoStoreInFirebase() {
        String id = "null";
        IStore store = StoresRepository.getInstance().fetchStoreById(id);
        assertNull(store);
    }
}
