package com.example.nolo.entities.user;

import java.util.List;

public class User implements IUser {
    private String userAuthUid, email;
    private List<String> viewHistoryIds, cartIds;

    /**
      * 0 argument constructor for convert Firebase data to this class
      */
    public User() {}

    public User(String userAuthUid, String email, List<String> viewHistoryIds, List<String> cartIds) {
        this.userAuthUid = userAuthUid;
        this.email = email;
        this.viewHistoryIds = viewHistoryIds;
        this.cartIds = cartIds;
    }

    @Override
    public String getUserAuthUid() {
        return userAuthUid;
    }

    @Override
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
}
