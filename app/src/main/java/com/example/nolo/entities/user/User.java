package com.example.nolo.entities.user;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

import java.util.List;

public class User implements IUser {
    // {userAuthUid, email} will not be in the Firestore
    private String userAuthUid, email;
    private List<String> viewHistoryIds, cartIds;

    /**
      * 0 argument constructor for convert Firebase data to this class
      */
    public User() {}

    public User(List<String> viewHistoryIds, List<String> cartIds) {
        this.viewHistoryIds = viewHistoryIds;
        this.cartIds = cartIds;
    }

    @Override
    public void setUserAuthUid(String userAuthUid) {
        this.userAuthUid = userAuthUid;
    }

    @Override
    @Exclude
    public String getUserAuthUid() {
        return userAuthUid;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    @Exclude
    public String getEmail() {
        return email;
    }

    @Override
    public List<String> getViewHistoryIds() {
        return viewHistoryIds;
    }

    @Override
    public void addViewHistory(String itemId) {
        viewHistoryIds.add(itemId);
    }

    public List<String> getCartIds() {
        return cartIds;
    }

    @Override
    public void addCart(String itemId) {
        cartIds.add(itemId);
    }

    @Override
    public void removeCart(String itemId) {
        cartIds.remove(itemId);
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "userAuthUid='" + userAuthUid + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
