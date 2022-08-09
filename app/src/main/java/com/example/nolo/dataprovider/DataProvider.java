package com.example.nolo.dataprovider;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.nolo.entities.store.Branch;
import com.example.nolo.entities.store.IBranch;
import com.example.nolo.entities.store.IStore;
import com.example.nolo.entities.store.Store;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DataProvider {
    public static void addStoreToFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        List<IBranch> b = new ArrayList<>();
        b.add(new Branch("botany", "(2, 3)"));
        b.add(new Branch("pakang", "(3, 2)"));
        IStore s = new Store("123", "asd", b);

        db.collection("stores").document(s.getStoreId()).set(s).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.i("Stores Collection Add", "store " + s.getStoreId() + " added.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Stores Collection Add", "store " + s.getStoreId() + " NOT added.");
            }
        });
    }
}
