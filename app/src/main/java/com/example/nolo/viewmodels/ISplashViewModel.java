package com.example.nolo.viewmodels;

import java.util.Set;

public interface ISplashViewModel {
    Set<Class<?>> getLoadable();
    Set<Class<?>> getLoaded();
    void addLoaded(Class<?> repoClass);
    float getLoadProgress();
}
