package com.example.nolo.repositories.user;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.entities.user.IUser;
import com.example.nolo.entities.user.User;
import com.example.nolo.enums.CollectionPath;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
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
    private static final String FIELD_VIEW_HISTORY = "viewHistory";
    private static final String FIELD_WISHLIST = "wishlist";
    private static final String FIELD_PURCHASE_HISTORY = "purchaseHistory";
    private static final String FIELD_CART = "cart";

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
            Log.i("UserRepository", "Current UID: " + currentFBUser.getUid());

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
        Log.i("UserRepository", "Not signed in");
        return null;
    }

    /**
     * After signing up, add user into Firestore.
     */
    private void addUserRepoAfterSignedUp(Consumer<String> onAddUserRepoComplete, String uid) {
        db.collection(CollectionPath.users.name()).document(uid).set(new User()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i("Add users to Firebase", uid + " added.");
                    IUser usr = new User();
                    usr.setUserAuthUid(uid);
                    usersRepo.add(usr);
                    onAddUserRepoComplete.accept(null);
                } else {
                    onAddUserRepoComplete.accept(task.getException().getMessage());
                }
            }
        });
    }

    /**
     * After signing up, it will automatically login the app
     */
    @Override
    public void signUp(Consumer<String> onUserSignedUp, String email, String password) {
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i("Sign Up", "createUserWithEmail:success");

                    // After signing up, first add user into Firestore and then login
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
    public void changePassword(Consumer<String> onUserChangePassword, String oldPassword, String newPassword) {
        FirebaseUser currentFBUser = fAuth.getCurrentUser();
        if (currentFBUser == null)
            return;

        AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), oldPassword);

        // First make sure the old password is correct, if so continue the process
        currentFBUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    // Old password is correct, now change the password
                    currentFBUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                onUserChangePassword.accept(null);
                            } else {
                                onUserChangePassword.accept(task.getException().getMessage());
                            }
                        }
                    });
                } else {
                    onUserChangePassword.accept("The current password is incorrect.");
                }
            }
        });
    }

    @Override
    public List<ItemVariant> getViewHistory() {
        return currentUser.getViewHistory();
    }

    /**
     * Add selected item (ItemVariant) into the user's view history
     *
     * @param item selected item (ItemVariant)
     */
    @Override
    public void addViewHistory(IItemVariant item) {
        currentUser.addViewHistory(item);
        updateFirebaseField(FIELD_VIEW_HISTORY, currentUser.getViewHistory());
    }

    @Override
    public List<ItemVariant> getWishlist() {
        return currentUser.getWishlist();
    }

    /**
     * Add wishlist items into wishlist at the top
     *
     * @param item wishlist items
     */
    @Override
    public void addWishlist(IItemVariant item) {
        currentUser.addWishlist(item);
        updateFirebaseField(FIELD_WISHLIST, currentUser.getWishlist());
    }

    /**
     * Remove item from the user's wishlist
     *
     * @param item The item to be removed
     */
    @Override
    public void removeWishlist(IItemVariant item) {
        currentUser.removeWishlist(item);
        updateFirebaseField(FIELD_WISHLIST, currentUser.getWishlist());
    }

    @Override
    public List<Purchasable> getPurchaseHistory() {
        return currentUser.getPurchaseHistory();
    }

    /**
     * Add purchased items into purchase history at the top
     *
     * @param purchasedItem purchased items
     */
    @Override
    public void addPurchaseHistory(List<Purchasable> purchasedItem) {
        currentUser.addPurchaseHistory(purchasedItem);
        updateFirebaseField(FIELD_PURCHASE_HISTORY, currentUser.getPurchaseHistory());
    }

    @Override
    public List<Purchasable> getCart() {
        return currentUser.getCart();
    }

    /**
     * Add selected item with quantity (Purchasable) into the user's cart
     *
     * @param cartItem selected item with quantity (Purchasable)
     */
    @Override
    public void addCart(IItemVariant cartItem, int quantity) {
        currentUser.addCart(cartItem, quantity);
        updateFirebaseField(FIELD_CART, currentUser.getCart());
    }

    /**
     * Update the user's cart with the new cart list
     *
     * @param cartItems New cart list
     */
    @Override
    public void updateCart(List<Purchasable> cartItems) {
        currentUser.updateCart(cartItems);
        updateFirebaseField(FIELD_CART, currentUser.getCart());
    }

    private void updateFirebaseField(String fieldName, List<?> listToStore) {
        if (currentUser.isFieldNameValid(fieldName)) {
            db.collection(CollectionPath.users.name()).document(currentUser.getUserAuthUid()).update(fieldName, listToStore);
        } else {
            Log.e("UsersRepository", "Unable to update " + fieldName + " as field not matched");
        }
    }
}
