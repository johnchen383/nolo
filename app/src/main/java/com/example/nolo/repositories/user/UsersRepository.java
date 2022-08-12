package com.example.nolo.repositories.user;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This is a singleton class for Users repository.
 */
public class UsersRepository implements IUsersRepository {
    public static final String COLLECTION_PATH_USERS = "users";

    private static UsersRepository usersRepository = null;
    private final FirebaseFirestore db;

    private UsersRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public static UsersRepository getInstance() {
        if (usersRepository == null)
            usersRepository = new UsersRepository();

        return usersRepository;
    }
}
