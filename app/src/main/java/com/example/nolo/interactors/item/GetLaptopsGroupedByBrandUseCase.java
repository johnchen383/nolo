package com.example.nolo.interactors.item;

import com.example.nolo.entities.item.IItem;
import com.example.nolo.entities.item.Laptop;
import com.example.nolo.enums.CategoryType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetLaptopsGroupedByBrandUseCase {
    public static List<IItem> getLaptopsGroupedByBrand(){
        List<IItem> laptopItems = GetCategoryItemsUseCase.getCategoryItems(CategoryType.laptops);
        Map<String, List<IItem>> brandMap = laptopItems.stream().collect(Collectors.groupingBy(IItem::getBrand));

        List<IItem> groupedLaptops = new ArrayList<>();
        for (String brand : brandMap.keySet()){
            groupedLaptops.addAll(brandMap.get(brand));
        }

        return groupedLaptops;
    }
}
