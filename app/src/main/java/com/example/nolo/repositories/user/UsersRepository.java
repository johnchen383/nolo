package com.example.nolo.repositories.user;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.nolo.entities.user.IUser;
import com.example.nolo.entities.user.User;
import com.example.nolo.repositories.CollectionPath;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * This is a singleton class for Users repository.
 */
public class UsersRepository implements IUsersRepository {
    private static UsersRepository usersRepository = null;
    private final FirebaseFirestore db;
    private final FirebaseAuth fAuth;
    private final List<IUser> usersRepo;
    private IUser currentUser;

    private UsersRepository() {
        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        usersRepo = new ArrayList<>();
        currentUser = null;
    }

    public static UsersRepository getInstance() {
        if (usersRepository == null)
            usersRepository = new UsersRepository();

        return usersRepository;
    }

    @Override
    public void loadUsers(Consumer<Class<?>> loadedRepository) {
        usersRepo.clear();

        db.collection(CollectionPath.users.name()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        IUser user = document.toObject(User.class);
                        user.setUserAuthUid(document.getId());  // store document ID after getting the object
                        usersRepo.add(user);
                        Log.i("Load Users From Firebase", user.toString());
                    }
                } else {
                    Log.i("Load Users From Firebase", "Loading Users collection failed from Firestore!");
                }

                // inform this repository finished loading
                loadedRepository.accept(UsersRepository.class);
            }
        });
    }

    /*
    * This method return the current user in User entity if the user is signed in,
    * if the user is not signed in, it will return null.
    */
    @Override
    public IUser getCurrentUser() {
        FirebaseUser currentFBUser = fAuth.getCurrentUser();

        // Check if user is signed in (non-null)
        if (currentFBUser != null) {
            String userAuthUid = currentFBUser.getUid();

            // Find the user in the user repository (Firestore)
            for (IUser u : usersRepo) {
                if (u.getUserAuthUid().equals(userAuthUid)) {
                    currentUser = u;
                    currentUser.setEmail(currentFBUser.getEmail());
                    return currentUser;
                }
            }
        }

        // Not signed in
        return null;
    }

    /*
     * After signing up, add user into Firestore.
     */
    private void addUserRepoAfterSignedUp(String uid) {
        db.collection(CollectionPath.users.name()).document(uid).set(new User()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i("Add users to Firebase", uid + " added.");
                } else {
                    Log.i("Add users to Firebase", uid + " NOT added.");
                }
            }
        });
    }

    @Override
    public void signUp(String email, String password) {
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i("Sign Up", "createUserWithEmail:success");

                    // after signing up, add user into Firestore
                    addUserRepoAfterSignedUp(task.getResult().getUser().getUid());
                } else {
                    Log.i("Sign Up", "createUserWithEmail:failure", task.getException());
                }
            }
        });
    }

    @Override
    public void logIn(Consumer<Class<?>> userLoggedIn, String email, String password) {
        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i("Log In", "signInWithEmail:success");
                } else {
                    Log.i("Log In", "signInWithEmail:failure", task.getException());
                }

                // inform this repository finished loading
                userLoggedIn.accept(UsersRepository.class);
            }
        });
    }

    @Override
    public void logOut() {
        fAuth.signOut();
    }

    // TODO: Adding duplicate IDs, then ???
    @Override
    public void addViewHistory(String itemId) {
        currentUser.getViewHistoryIds().add(itemId);

        // Add array value
        db.collection(CollectionPath.users.name()).document(currentUser.getUserAuthUid())
                .update("viewHistoryIds", FieldValue.arrayUnion(itemId));
    }

    // TODO: Adding duplicate IDs, then ???
    @Override
    public void addCart(String itemId) {
        currentUser.getCartIds().add(itemId);

        // Add array value
        db.collection(CollectionPath.users.name()).document(currentUser.getUserAuthUid())
                .update("cartIds", FieldValue.arrayUnion(itemId));
    }

    // TODO: Remove ID that is duplicate, then ???
    @Override
    public void removeCart(String itemId) {
        currentUser.getCartIds().remove(itemId);

        // Remove array value
        db.collection(CollectionPath.users.name()).document(currentUser.getUserAuthUid())
                .update("cartIds", FieldValue.arrayRemove(itemId));
    }
}
