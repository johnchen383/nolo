package com.example.nolo.entities.user;

import java.util.List;

public interface IUser {
    void setUserAuthUid(String userAuthUid);
    String getUserAuthUid();
    void setEmail(String email);
    String getEmail();
    List<String> getViewHistoryIds();
    void addViewHistory(String itemId);
    List<String> getCartIds();
    void addCart(String itemId);
    void removeCart(String itemId);
}
