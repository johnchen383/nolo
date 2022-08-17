package com.example.nolo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.nolo.interactors.LoadStoresRepositoryUseCase;
import com.example.nolo.repositories.store.StoresRepository;
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
public class StoresInstrumentedTest {
    Context appContext;
    CountDownLatch lock;

    @Before
    public void setUp(){
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        lock = new CountDownLatch(1);
        FirebaseApp.initializeApp(appContext);
    }

    @Test
    public void testPackageName() {
        assertEquals("com.example.nolo", appContext.getPackageName());
    }

    @Test
    public void testLoadingStoresData() throws Exception {
        List<Class<?>> str = new ArrayList<>();

        LoadStoresRepositoryUseCase.loadStoresRepository((cName) -> {
            str.add(cName);
            lock.countDown();
        });

        lock.await(20000, TimeUnit.MILLISECONDS);

        // Test if the Firebase data is loaded
        assertEquals(StoresRepository.class, str.get(0));
        assertTrue(new SplashViewModel().getLoadable().contains(StoresRepository.class));
    }

    //TODO: testing the fetching of store by id once items are inserted
}