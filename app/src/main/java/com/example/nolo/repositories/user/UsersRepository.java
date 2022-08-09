package com.example.nolo.repositories.user;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This is a singleton class for Users repository.
 */
public class UsersRepository implements IUsersRepository {
    private static UsersRepository usersRepository = null;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private UsersRepository() {}

    public static UsersRepository getInstance() {
        if (usersRepository == null)
            usersRepository = new UsersRepository();

        return usersRepository;
    }
}
