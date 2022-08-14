package com.example.nolo.repositories.user;

import com.example.nolo.entities.item.IItemVariant;
import com.example.nolo.entities.item.IPurchasable;
import com.example.nolo.entities.user.IUser;

import java.util.function.Consumer;

public interface IUsersRepository {
    void loadUsers(Consumer<Class<?>> loadedRepository);
    IUser getCurrentUser();
    void signUp(Consumer<String> userSignedUp, String email, String password);
    void logIn(Consumer<String> userLoggedIn, String email, String password);
    void logOut();
    void addViewHistory(IItemVariant item);
    void addCart(IPurchasable cartItem);
    void removeCart(IPurchasable cartItem);
}
