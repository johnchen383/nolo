package com.example.nolo.repositories.user;

import com.example.nolo.entities.user.IUser;

public interface IUsersRepository {
    IUser getCurrentUser();
    void signUp(String email, String password);
    void logIn(String email, String password);
    void logOut();
    void addViewHistory(String itemId);
    void addCart(String itemId);
    void removeCart(String itemId);
}
