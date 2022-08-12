package com.example.nolo.repositories.user;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.nolo.entities.user.IUser;
import com.example.nolo.entities.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * This is a singleton class for Users repository.
 */
public class UsersRepository implements IUsersRepository {
    public static final String COLLECTION_PATH_USERS = "users";

    private static UsersRepository usersRepository = null;
    private final FirebaseFirestore db;
    private final FirebaseAuth fAuth;

    private UsersRepository() {
        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
    }

    public static UsersRepository getInstance() {
        if (usersRepository == null)
            usersRepository = new UsersRepository();

        return usersRepository;
    }

    @Override
    public IUser getCurrentUser() {
        FirebaseUser currentUser = fAuth.getCurrentUser();

        // Check if user is signed in (non-null)
        if (currentUser != null) {
            String userAuthUid = currentUser.getUid();
            String email = currentUser.getEmail();
            List<String> viewHistoryIds = new ArrayList<>();
            List<String> cartIds = new ArrayList<>();

            IUser user = new User(userAuthUid, email, viewHistoryIds, cartIds);

            return user;
        }

        return null;
    }

    @Override
    public void signUp(String email, String password) {
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i("Sign Up", "createUserWithEmail:success");
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

    @Override
    public void addViewHistory(String itemId) {

    }

    @Override
    public void addCart(String itemId) {

    }

    @Override
    public void removeCart(String itemId) {

    }
}
