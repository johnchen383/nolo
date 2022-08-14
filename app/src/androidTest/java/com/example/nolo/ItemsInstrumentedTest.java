package com.example.nolo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.nolo.interactors.GetCategoriesUseCase;
import com.example.nolo.interactors.GetCategoryByIdUseCase;
import com.example.nolo.interactors.GetCategoryItemsUseCase;
import com.example.nolo.interactors.LoadItemsRepositoryUseCase;
import com.example.nolo.repositories.CategoryType;
import com.example.nolo.repositories.item.ItemsRepository;
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
public class ItemsInstrumentedTest {
    Context appContext;
    CountDownLatch lock;

    @Before
    public void setUp(){
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        lock = new CountDownLatch(1);
        FirebaseApp.initializeApp(appContext);
    }

    @Test
    public void testLoadingItemsData() throws Exception {
        List<Class<?>> str = new ArrayList<>();

        LoadItemsRepositoryUseCase.loadItemsRepository((cName) -> {
            str.add(cName);
            lock.countDown();
        });

        lock.await(20000, TimeUnit.MILLISECONDS);

        // Test if the Firebase data is loaded
        assertEquals(ItemsRepository.class, str.get(0));
        assertTrue(new SplashViewModel().getLoadable().contains(ItemsRepository.class));

        // Test getCategoryType()
        assertSame(CategoryType.laptops, GetCategoryItemsUseCase.getCategoryItems(CategoryType.laptops).get(0).getCategoryType());
        assertSame(CategoryType.phones, GetCategoryItemsUseCase.getCategoryItems(CategoryType.phones).get(0).getCategoryType());
        assertSame(CategoryType.accessories, GetCategoryItemsUseCase.getCategoryItems(CategoryType.accessories).get(0).getCategoryType());

        // Test getCategoryById()
        CategoryType id = GetCategoriesUseCase.getCategories().get(0).getCategoryType();
        assertNotNull(GetCategoryByIdUseCase.getCategoryById(id));
    }

}