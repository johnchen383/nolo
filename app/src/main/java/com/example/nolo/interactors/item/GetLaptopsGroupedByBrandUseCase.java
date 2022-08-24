package com.example.nolo.interactors.item;

import com.example.nolo.entities.item.IItem;
import com.example.nolo.enums.CategoryType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetLaptopsGroupedByBrandUseCase {
    /**
     * Get all laptops that are grouped by brands in alphabetical order
     *
     * @return Laptops that are grouped by brands in alphabetical order
     */
    public static List<List<IItem>> getLaptopsGroupedByBrand() {
        List<IItem> laptopItems = GetCategoryItemsUseCase.getCategoryItems(CategoryType.laptops);

        // Group the laptops by brands
        Map<String, List<IItem>> brandMap = laptopItems.stream().collect(Collectors.groupingBy(IItem::getBrand));

        // Sort the brands name in alphabetical order
        List<String> brands = new ArrayList<>(brandMap.keySet());
        Collections.sort(brands);

        List<List<IItem>> groupedLaptops = new ArrayList<>();
        for (String brand : brands) {
            groupedLaptops.add(brandMap.get(brand));
        }

        return groupedLaptops;
    }
}
