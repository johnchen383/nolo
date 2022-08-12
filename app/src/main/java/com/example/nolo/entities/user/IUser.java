package com.example.nolo.entities.user;

import java.util.List;

public interface IUser {
    String getUserId();
    String getUserAuthUid();
    String getEmail();
    List<String> getViewHistoryIds();
    void addViewHistory(String itemId);
    List<String> getCartIds();
    void addCart(String itemId);
    void removeCart(String itemId);
}
