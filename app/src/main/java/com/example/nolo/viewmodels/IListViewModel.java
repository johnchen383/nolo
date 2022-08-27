package com.example.nolo.viewmodels;

import com.example.nolo.entities.category.ICategory;
import com.example.nolo.enums.PhoneOs;

public interface IListViewModel {
    ICategory getCategory();
    PhoneOs getPhoneOs();
    void setPhoneOs(PhoneOs os);
}
