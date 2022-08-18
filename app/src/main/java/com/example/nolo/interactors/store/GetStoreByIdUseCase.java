package com.example.nolo.interactors.store;

import com.example.nolo.entities.store.IStore;
import com.example.nolo.repositories.store.StoresRepository;

public class GetStoreByIdUseCase {
    /**
     *
     * @param storeId
     * @return
     */
    public static IStore getStoreById(String storeId) {
        return StoresRepository.getInstance().getStoreById(storeId);
    }
}
