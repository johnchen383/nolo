package com.example.nolo.entities;

import java.util.List;

public class User implements IUser {
    private String userId, userAuthUid, email;
    private List<String> viewHistoryIds, cartIds;

    public User() {}

    public User(String userId, String userAuthUid, String email, List<String> viewHistoryIds, List<String> cartIds) {
        this.userId = userId;
        this.userAuthUid = userAuthUid;
        this.email = email;
        this.viewHistoryIds = viewHistoryIds;
        this.cartIds = cartIds;
    }

    @Override
    public String getUserId() {
        return userId;
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
    public void addViewHistory(String id) {
        viewHistoryIds.add(id);
    }

    public List<String> getCartIds() {
        return cartIds;
    }

    @Override
    public void addCart(String id) {
        cartIds.add(id);
    }

    @Override
    public void removeCart(String id) {
        cartIds.remove(id);
    }
}
