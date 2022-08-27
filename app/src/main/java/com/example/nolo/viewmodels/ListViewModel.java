package com.example.nolo.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.nolo.entities.category.ICategory;
import com.example.nolo.enums.PhoneOs;

public class ListViewModel extends ViewModel implements IListViewModel {
    private PhoneOs phoneOs = PhoneOs.android;
    private ICategory category;

    public ListViewModel(ICategory category) {
        this.category = category;
    }

    @Override
    public ICategory getCategory() {
        return category;
    }

    @Override
    public PhoneOs getPhoneOs() {
        return phoneOs;
    }

    @Override
    public void setPhoneOs(PhoneOs os) {
        phoneOs = os;
    }
}
