package com.example.nolo.interactors.item;

import com.example.nolo.repositories.item.ItemsRepository;

import java.util.function.Consumer;

public class LoadItemsRepositoryUseCase {
    public static void loadItemsRepository(Consumer<Class<?>> loadedRepository) {
        ItemsRepository.getInstance().loadItems(loadedRepository);
    }
}
