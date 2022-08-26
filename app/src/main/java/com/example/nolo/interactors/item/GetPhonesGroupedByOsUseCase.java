package com.example.nolo.interactors.item;

import com.example.nolo.entities.item.IItem;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.enums.PhoneOs;
import com.example.nolo.enums.SpecsType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetPhonesGroupedByOsUseCase {
    /**
     * Get phones of the target operating system
     *
     * @param targetOs Target operating system
     * @return Phones of the target operating system
     */
    public static List<List<IItem>> getPhonesGroupedByOs(PhoneOs targetOs) {
        List<IItem> phoneItems = GetCategoryItemsUseCase.getCategoryItems(CategoryType.phones);

        // Group the phones by operating systems
        Map<String, List<IItem>> osMap = phoneItems.stream().collect(Collectors.groupingBy(
                (item) -> item.getSpecs().getFixedSpecs().get(SpecsType.operatingSystem)
        ));

        // Only get the phones of the target OS
        List<List<IItem>> groupedPhones = new ArrayList<>();
        for (String os : osMap.keySet()) {
            if (os.equals(targetOs.name())) {
                int count = 0;
                List<IItem> tuple = new ArrayList<>();

                for (IItem phone : osMap.get(os)) {
                    tuple.add(phone);

                    count++;
                    if (count % 2 == 0) {
                        groupedPhones.add(tuple);
                        tuple = new ArrayList<>();
                    }
                }

                if (!tuple.isEmpty()) groupedPhones.add(tuple);

                break;
            }
        }

        return groupedPhones;
    }
}
