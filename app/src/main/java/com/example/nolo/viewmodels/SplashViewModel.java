package com.example.nolo.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.nolo.repositories.category.CategoriesRepository;
import com.example.nolo.repositories.item.ItemsRepository;
import com.example.nolo.repositories.store.StoresRepository;
import com.example.nolo.repositories.user.UsersRepository;

import java.util.HashSet;
import java.util.Set;

public class SplashViewModel extends ViewModel implements ISplashViewModel {
    Set<Class<?>> loadable = new HashSet<>();
    Set<Class<?>> loaded = new HashSet<>();

    public SplashViewModel(){
        loadable.add(StoresRepository.class);
        loadable.add(CategoriesRepository.class);
        loadable.add(UsersRepository.class);
        loadable.add(ItemsRepository.class);
    }

    @Override
    public Set<Class<?>> getLoadable(){
        return this.loadable;
    }

    @Override
    public Set<Class<?>> getLoaded(){
        return this.loaded;
    }

    @Override
    public void addLoaded(Class<?> repoClass){
        loaded.add(repoClass);
    }

    @Override
    public float getLoadProgress(){
        return ((float)loaded.size()) / ((float) loadable.size());
    }
}
