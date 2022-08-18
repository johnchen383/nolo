package com.example.nolo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.nolo.entities.item.IItem;
import com.example.nolo.interactors.category.GetCategoriesUseCase;
import com.example.nolo.interactors.category.GetCategoryByTypeUseCase;
import com.example.nolo.interactors.item.GetAccessRecommendationsByItemIdUseCase;
import com.example.nolo.interactors.item.GetAllItemsUseCase;
import com.example.nolo.interactors.item.GetCategoryItemsUseCase;
import com.example.nolo.interactors.item.GetItemByIdUseCase;
import com.example.nolo.interactors.item.LoadItemsRepositoryUseCase;
import com.example.nolo.enums.CategoryType;
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

        // Test getAllItems()
        assertTrue(GetAllItemsUseCase.getAllItems().size() > 0);

        // Test getItemById()
        IItem i1 = GetAllItemsUseCase.getAllItems().get(0);
        IItem i2 = GetItemByIdUseCase.getItemById(i1.getItemId());
        assertEquals(i1.getItemId(), i2.getItemId());
        assertEquals(i1.getName(), i2.getName());

        // Test getCategoryItems()
        assertTrue(GetCategoryItemsUseCase.getCategoryItems(CategoryType.laptops).size() > 0);
        assertTrue(GetCategoryItemsUseCase.getCategoryItems(CategoryType.phones).size() > 0);
        assertTrue(GetCategoryItemsUseCase.getCategoryItems(CategoryType.accessories).size() > 0);

        // Test Item.getCategoryType()
        assertSame(CategoryType.laptops, GetCategoryItemsUseCase.getCategoryItems(CategoryType.laptops).get(0).getCategoryType());
        assertSame(CategoryType.phones, GetCategoryItemsUseCase.getCategoryItems(CategoryType.phones).get(0).getCategoryType());
        assertSame(CategoryType.accessories, GetCategoryItemsUseCase.getCategoryItems(CategoryType.accessories).get(0).getCategoryType());
    }

}
