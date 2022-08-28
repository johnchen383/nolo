package com.example.nolo.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.nolo.entities.category.ICategory;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.enums.PhoneOs;
import com.example.nolo.interactors.item.GetCategoryItemsUseCase;
import com.example.nolo.interactors.item.GetLaptopsGroupedByBrandUseCase;
import com.example.nolo.interactors.item.GetPhonesGroupedByOsUseCase;

import java.util.ArrayList;
import java.util.List;

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
    public CategoryType getCategoryType() {
        return category.getCategoryType();
    }
    @Override
    public void setPhoneOs(PhoneOs os) {
        phoneOs = os;
    }

    @Override
    public List<List<IItem>> getGroupedList(CategoryType categoryType) {
        switch (categoryType) {
            case laptops:
                return GetLaptopsGroupedByBrandUseCase.getLaptopsGroupedByBrand();
            case phones:
                return GetPhonesGroupedByOsUseCase.getPhonesGroupedByOs(phoneOs);
            default:
                List<List<IItem>> items = new ArrayList<>();
                List<IItem> tempItems = GetCategoryItemsUseCase.getCategoryItems(CategoryType.accessories);
                for (IItem tempItem : tempItems) {
                    List<IItem> l = new ArrayList<>();
                    l.add(tempItem);
                    items.add(l);
                }
                return items;
        }
    }
}
