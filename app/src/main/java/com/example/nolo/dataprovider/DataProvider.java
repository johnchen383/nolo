package com.example.nolo.dataprovider;

import static com.example.nolo.repositories.store.StoresRepository.COLLECTION_PATH_STORES;
import static com.example.nolo.repositories.user.UsersRepository.COLLECTION_PATH_USERS;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.nolo.entities.store.Branch;
import com.example.nolo.entities.store.IStore;
import com.example.nolo.entities.store.Store;
import com.example.nolo.entities.user.IUser;
import com.example.nolo.entities.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class DataProvider {
    private static List<IStore> generateStores() {
        List<IStore> stores = new ArrayList<>();

        List<Branch> branches = new ArrayList<>();
        branches.add(new Branch("botany", "17 Botany Street", new GeoPoint(3, 5)));
        branches.add(new Branch("pakang", "51 Pak Lane", new GeoPoint(10, 8)));
        stores.add(new Store("pbtech", branches));

        branches = new ArrayList<>();
        branches.add(new Branch("manuka", "9 Manuka Ave", new GeoPoint(-9, -15)));
        stores.add(new Store("jbhifi", branches));

        return stores;
    }

    public static void addStoresToFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<IStore> stores = generateStores();

        for (IStore store : stores) {
            db.collection(COLLECTION_PATH_STORES).add(store).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        Log.i("Add stores to Firebase", task.getResult().getId() + " added.");
                    } else {
                        Log.i("Add stores to Firebase", store + " NOT added.");
                    }
                }
            });
        }
    }

    private static List<IUser> generateUsers() {
        List<IUser> users = new ArrayList<>();
        List<String> historyIds, cartIds;
        IUser u;

        historyIds = List.of("11", "12", "13");
        cartIds = List.of("1", "2", "3", "4");
        u = new User(historyIds, cartIds);
        u.setEmail("john@gmail.com");
        u.setUserAuthUid("john");  // userAuthUid here is used for password
        users.add(u);

        historyIds = List.of("22", "33", "44");
        cartIds = List.of("2", "3", "4", "5");
        u = new User(historyIds, cartIds);
        u.setEmail("nick@gmail.com");
        u.setUserAuthUid("nick");  // userAuthUid here is used for password
        users.add(u);

        return users;
    }

    public static void addUsersToFirestore() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<IUser> users = generateUsers();

        for (IUser user : users) {
            auth.createUserWithEmailAndPassword(user.getEmail(), user.getUserAuthUid()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        user.setUserAuthUid(task.getResult().getUser().getUid());
                        Log.i("Sign Up", "createUserWithEmail:success");

                        db.collection(COLLECTION_PATH_USERS).document(user.getUserAuthUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.i("Add users to Firebase", user.getUserAuthUid() + " added.");
                                } else {
                                    Log.i("Add users to Firebase", user + " NOT added.");
                                }
                            }
                        });
                    } else {
                        Log.i("Sign Up", "createUserWithEmail:failure", task.getException());
                    }
                }
            });
        }
    }
}
