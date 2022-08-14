package com.example.nolo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.nolo.entities.user.IUser;
import com.example.nolo.entities.user.User;
import com.example.nolo.interactors.GetCategoriesUseCase;
import com.example.nolo.interactors.GetCategoryByIdUseCase;
import com.example.nolo.interactors.GetCurrentUserUseCase;
import com.example.nolo.interactors.LoadCategoriesRepositoryUseCase;
import com.example.nolo.interactors.LoadUsersRepositoryUseCase;
import com.example.nolo.interactors.LogInUseCase;
import com.example.nolo.interactors.LogOutUseCase;
import com.example.nolo.interactors.SignUpUseCase;
import com.example.nolo.repositories.category.CategoriesRepository;
import com.example.nolo.repositories.user.UsersRepository;
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
public class UsersInstrumentedTest {
    Context appContext;
    CountDownLatch lock;

    @Before
    public void setUp(){
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        lock = new CountDownLatch(1);
        FirebaseApp.initializeApp(appContext);
    }

    //assumes john.bm.chen@gmail.com password123 is in firebase
    @Test
    public void testLoadingUsersData() throws Exception {
        List<Class<?>> str = new ArrayList<>();

        //test load
        LoadUsersRepositoryUseCase.loadUsersRepository((cName) -> {
            str.add(cName);
            lock.countDown();
        });

        lock.await(20000, TimeUnit.MILLISECONDS);
        assertEquals(UsersRepository.class, str.get(0));
        assertTrue(new SplashViewModel().getLoadable().contains(UsersRepository.class));

        //test log out
        IUser usr = GetCurrentUserUseCase.getCurrentUser();
        if (usr != null){
            LogOutUseCase.logOut();
            usr = GetCurrentUserUseCase.getCurrentUser();
            assertTrue(usr == null);
        }

        List<String> errs = new ArrayList<>();
        //test log in
        lock = new CountDownLatch(1);
        LogInUseCase.logIn((err) -> {
            errs.add(err);
            lock.countDown();
        }, "john.am.asdasdsad@gmail.com", "asas");

        lock.await(20000, TimeUnit.MILLISECONDS);
        if (errs.get(0) == null) fail();

        lock = new CountDownLatch(1);
        errs.clear();
        LogInUseCase.logIn((err) -> {
            errs.add(err);
            lock.countDown();
        }, "john.bm.chen@gmail.com", "password123");

        lock.await(20000, TimeUnit.MILLISECONDS);
        if (errs.get(0) != null) fail();

        //test log out
        usr = GetCurrentUserUseCase.getCurrentUser();
        if (usr != null){
            LogOutUseCase.logOut();
            usr = GetCurrentUserUseCase.getCurrentUser();
            assertTrue(usr == null);
        } else {
            fail();
        }

        //test sign up
        lock = new CountDownLatch(1);
        errs.clear();
        SignUpUseCase.signUp((err) -> {
            errs.add(err);
            lock.countDown();
        }, "john.bm.chen@gmail.com", "password123");

        lock.await(20000, TimeUnit.MILLISECONDS);
        if (errs.get(0) == null) fail();
    }

}