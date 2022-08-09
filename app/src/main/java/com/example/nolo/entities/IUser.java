package com.example.nolo.entities;

import java.util.List;

public interface IUser {
    String getUserId();
    String getUserAuthUid();
    String getEmail();
    List<String> getViewHistoryIds();
    void addViewHistory(String id);
    List<String> getCartIds();
    void addCart(String id);
    void removeCart(String id);
}
