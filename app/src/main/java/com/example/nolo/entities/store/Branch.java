package com.example.nolo.entities.store;

import com.google.firebase.firestore.GeoPoint;

public class Branch implements IBranch {
    private String branchName, address;
    private GeoPoint geoPoint;

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public Branch() {}

    public Branch(String branchName, String address, GeoPoint geoPoint) {
        this.branchName = branchName;
        this.address = address;
        this.geoPoint = geoPoint;
    }

    @Override
    public String getBranchName() {
        return branchName;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public GeoPoint getGeoPoint() {
        return geoPoint;
    }
}
