package com.example.nolo.entities.store;

import com.google.firebase.firestore.GeoPoint;

public interface IBranch {
    String getBranchName();
    String getAddress();
    GeoPoint getGeoPoint();
}
