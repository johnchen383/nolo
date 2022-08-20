package com.example.nolo.repositories.user;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.purchasable.IPurchasable;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.entities.user.IUser;
import com.example.nolo.entities.user.User;
import com.example.nolo.enums.CollectionPath;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    /**
     * This is for singleton class.
     */
    public static UsersRepository getInstance() {
        if (usersRepository == null)
            usersRepository = new UsersRepository();

        return usersRepository;
    }

    /**
     * Load data from Firebase.
     */
    @Override
    public void loadUsers(Consumer<Class<?>> onLoadedRepository) {
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
                onLoadedRepository.accept(UsersRepository.class);
            }
        });
    }

    /**
     * This method return the current user in User entity if the user is signed in,
     * if the user is not signed in, it will return null.
     *
     * @return Current user if signed in;
     *         Otherwise null
     */
    @Override
    public IUser getCurrentUser() {
        FirebaseUser currentFBUser = fAuth.getCurrentUser();

        // Check if user is signed in (non-null)
        if (currentFBUser != null) {
            String userAuthUid = currentFBUser.getUid();
            System.out.println("Current UID: " + currentFBUser.getUid());

            // Find the user in the user repository (Firestore)
            for (IUser u : usersRepo) {
                if (u.getUserAuthUid().equals(userAuthUid)) {
                    currentUser = u;
                    currentUser.setEmail(currentFBUser.getEmail());
                    return currentUser;
                }
            }
        }

        System.out.println("Not signed in");
        // Not signed in
        return null;
    }

    /**
     * After signing up, add user into Firestore.
     */
    private void addUserRepoAfterSignedUp(Consumer<String> onUserSignUp, String uid) {
        db.collection(CollectionPath.users.name()).document(uid).set(new User()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i("Add users to Firebase", uid + " added.");
                    IUser usr = new User();
                    usr.setUserAuthUid(uid);
                    usersRepo.add(usr);
                    onUserSignUp.accept(null);
                } else {
                    onUserSignUp.accept(task.getException().getMessage());
                }
            }
        });
    }

    @Override
    public void signUp(Consumer<String> onUserSignedUp, String email, String password) {
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i("Sign Up", "createUserWithEmail:success");
                    // after signing up, add user into Firestore
                    addUserRepoAfterSignedUp((addFirebaseErr) -> {
                        logIn((logInErr) -> {
                            if (logInErr != null) {
                                onUserSignedUp.accept(logInErr);
                            } else {
                                onUserSignedUp.accept(addFirebaseErr);
                            }
                        }, email, password);
                    }, task.getResult().getUser().getUid());
                } else {
                    onUserSignedUp.accept(task.getException().getMessage());
                }
            }
        });
    }

    @Override
    public void logIn(Consumer<String> onUserLoggedIn, String email, String password) {
        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i("Log In", "signInWithEmail:success");
                    currentUser = getCurrentUser();
                    onUserLoggedIn.accept(null);
                } else {
                    onUserLoggedIn.accept(task.getException().getMessage());
                }
            }
        });
    }

    @Override
    public void logOut() {
        fAuth.signOut();
        currentUser = null;
    }

    @Override
    public void addViewHistory(IItemVariant item) {
        currentUser.addViewHistory(item);

        String field = "viewHistory";
        if (currentUser.isFieldNameValid(field)){
            db.collection(CollectionPath.users.name()).document(currentUser.getUserAuthUid()).update(field, currentUser.getViewHistory());
        } else {
            Log.i("Err", "Unable to update view history as field not matched");
        }
    }

    @Override
    public void addCart(IPurchasable cartItem) {
        currentUser.addCart(cartItem);

        String field = "cart";
        if (currentUser.isFieldNameValid(field)){
            db.collection(CollectionPath.users.name()).document(currentUser.getUserAuthUid()).update(field, currentUser.getCart());
        } else {
            Log.i("Err", "Unable to update cart as field not matched");
        }
    }

    @Override
    public void removeCart(IPurchasable cartItem) {
        currentUser.removeCart(cartItem);

        String field = "cart";
        if (currentUser.isFieldNameValid(field)){
            db.collection(CollectionPath.users.name()).document(currentUser.getUserAuthUid()).update(field, currentUser.getCart());
        } else {
            Log.i("Err", "Unable to update cart as field not matched");
        }
    }

    @Override
    public List<ItemVariant> getViewHistory() {
        return currentUser.getViewHistory();
    }

    @Override
    public List<Purchasable> getCart() {
        return currentUser.getCart();
    }
}
