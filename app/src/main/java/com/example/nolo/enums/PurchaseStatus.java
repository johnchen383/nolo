package com.example.nolo.enums;

public enum PurchaseStatus {
    inCart("In Cart"),
    inTransit("In Transit"),
    delivered("Delivered");

    private final String fullname;

    PurchaseStatus(String fullname) {
        this.fullname = fullname;
    }

    public String getFullname() {
        return this.fullname;
    };
}
