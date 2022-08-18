package com.example.nolo.interactors.store;

import com.example.nolo.entities.store.IStore;
import com.example.nolo.repositories.store.StoresRepository;

public class GetStoreByIdUseCase {
    /**
     * Get Store entity by store ID
     *
     * @param storeId store ID
     * @return Store entity if storeId exists;
     *         Otherwise null
     */
    public static IStore getStoreById(String storeId) {
        return StoresRepository.getInstance().getStoreById(storeId);
    }
}
