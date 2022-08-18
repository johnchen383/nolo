package com.example.nolo.interactors.item;

import com.example.nolo.repositories.item.ItemsRepository;

import java.util.function.Consumer;

public class LoadItemsRepositoryUseCase {
    /**
     * Load data from Firebase
     *
     * @param onLoadedRepository A function that will run after the repository is loaded
     */
    public static void loadItemsRepository(Consumer<Class<?>> onLoadedRepository) {
        ItemsRepository.getInstance().loadItems(onLoadedRepository);
    }
}
