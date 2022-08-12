package com.example.nolo;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.nolo.interactors.LoadStoresRepositoryUseCase;
import com.example.nolo.repositories.store.StoresRepository;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    Context appContext;
    CountDownLatch lock = new CountDownLatch(1);

    @Before
    public void setUp(){
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
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
        assertEquals(StoresRepository.class, str.get(0));
    }
}