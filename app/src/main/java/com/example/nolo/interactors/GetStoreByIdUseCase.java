package com.example.nolo.interactors;

import com.example.nolo.entities.store.IStore;
import com.example.nolo.repositories.store.StoresRepository;

public interface GetStoreByIdUseCase {
    static IStore getStoreById(String storeId) {
        return StoresRepository.getInstance().getStoreById(storeId);
    }
}
