package com.example.nolo.viewmodels;

import com.example.nolo.entities.category.ICategory;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.enums.PhoneOs;

import java.util.List;

public interface IListViewModel {
    ICategory getCategory();
    CategoryType getCategoryType();
    void setPhoneOs(PhoneOs os);
    List<List<IItem>> getGroupedList(CategoryType categoryType);
}
