package com.example.nolo.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.nolo.entities.category.ICategory;
import com.example.nolo.enums.PhoneOs;
import com.example.nolo.repositories.category.CategoriesRepository;
import com.example.nolo.repositories.item.ItemsRepository;
import com.example.nolo.repositories.store.StoresRepository;
import com.example.nolo.repositories.user.UsersRepository;

import java.util.HashSet;
import java.util.Set;

public class ListViewModel extends ViewModel {
    private PhoneOs phoneOs = PhoneOs.android;
    private ICategory category;

    public ListViewModel(ICategory category){
        this.category = category;
    }

    public ICategory getCategory() {
        return category;
    }

    public PhoneOs getPhoneOs(){
        return phoneOs;
    }

    public void setPhoneOs(PhoneOs os){
        phoneOs = os;
    }
}
