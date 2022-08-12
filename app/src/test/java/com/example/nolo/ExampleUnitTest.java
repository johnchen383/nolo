package com.example.nolo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import android.app.Application;
import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.nolo.interactors.LoadStoresRepositoryUseCase;
import com.example.nolo.repositories.store.StoresRepository;
import com.google.firebase.FirebaseApp;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void test(){
        fail();
    }
}