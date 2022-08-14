package com.example.nolo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.nolo.interactors.GetCategoriesUseCase;
import com.example.nolo.interactors.GetCategoryByIdUseCase;
import com.example.nolo.interactors.LoadCategoriesRepositoryUseCase;
import com.example.nolo.repositories.CategoryType;
import com.example.nolo.repositories.category.CategoriesRepository;
import com.example.nolo.viewmodels.SplashViewModel;
import com.google.firebase.FirebaseApp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Note no integration tests are automatically run by gradle GitHub actions
 */
@RunWith(AndroidJUnit4.class)
public class CategoriesInstrumentedTest {
    Context appContext;
    CountDownLatch lock;

    @Before
    public void setUp(){
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        lock = new CountDownLatch(1);
        FirebaseApp.initializeApp(appContext);
    }

    @Test
    public void testLoadingCategoriesData() throws Exception {
        List<Class<?>> str = new ArrayList<>();

        LoadCategoriesRepositoryUseCase.loadCategoriesRepository((cName) -> {
            str.add(cName);
            lock.countDown();
        });

        lock.await(20000, TimeUnit.MILLISECONDS);
        assertEquals(CategoriesRepository.class, str.get(0));
        assertTrue(new SplashViewModel().getLoadable().contains(CategoriesRepository.class));
        assertTrue(GetCategoriesUseCase.getCategories().size() == 3);
        CategoryType id = GetCategoriesUseCase.getCategories().get(0).getCategoryType();
        assertTrue(GetCategoryByIdUseCase.getCategoryById(id) != null);
    }

}