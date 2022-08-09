package com.example.nolo.entities.store;

public class Branch implements IBranch {
    private String branchName, address;

    public Branch() {}

    public Branch(String branchName, String address) {
        this.branchName = branchName;
        this.address = address;
    }

    @Override
    public String getBranchName() {
        return branchName;
    }

    @Override
    public String getAddress() {
        return address;
    }
}
