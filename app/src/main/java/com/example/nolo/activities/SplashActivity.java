package com.example.nolo.activities;

import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.nolo.R;
import com.example.nolo.interactors.GetCategoriesUseCase;
import com.example.nolo.interactors.GetCurrentUserUseCase;
import com.example.nolo.interactors.LoadCategoriesRepositoryUseCase;
import com.example.nolo.interactors.LoadStoresRepositoryUseCase;
import com.example.nolo.interactors.LoadUsersRepositoryUseCase;
import com.example.nolo.interactors.LogInUseCase;
import com.example.nolo.interactors.LogOutUseCase;
import com.example.nolo.interactors.SignUpUseCase;
import com.example.nolo.viewmodels.CartViewModel;
import com.example.nolo.viewmodels.SplashViewModel;

public class SplashActivity extends BaseActivity {
    private SplashViewModel splashViewModel;

    private void onLoadRepoCacheComplete(Class<?> repoClass){
        splashViewModel.addLoaded(repoClass);

        if (splashViewModel.getLoadable().equals(splashViewModel.getLoaded())){
            System.out.println("All loaded");

            ActivityOptionsCompat fadeAnimOptions = ActivityOptionsCompat.makeCustomAnimation(this,
                    android.R.anim.fade_in, android.R.anim.fade_out);

            if (GetCurrentUserUseCase.getCurrentUser() != null){
                //navigate to main if already signed in
                System.out.println("YAAAAA");
                startActivity(new Intent(this, MainActivity.class), fadeAnimOptions.toBundle());
            } else {
                //navigate to sign in if not signed in
                System.out.println("YOOOOOO");
                startActivity(new Intent(this, LogInActivity.class), fadeAnimOptions.toBundle());

                //sample code for log in
//                LogInUseCase.logIn((error) -> {
//                    if (error == null){
//                        startActivity(new Intent(this, MainActivity.class), fadeAnimOptions.toBundle()); //TODO: change to sign in
//                    } else {
//                        //display error message
//                        System.out.println("ERR: " + error);
//                    }
//                }, "john.bm.chen@gmail.com", "password124");

                //sample code for sign up
//                SignUpUseCase.signUp((error) -> {
//                    if (error == null){
//                        startActivity(new Intent(this, MainActivity.class), fadeAnimOptions.toBundle()); //TODO: change to sign in
//                    } else {
//                        //display error message
//                        System.out.println("ERR: " + error);
//                    }
//                }, "johnd.bm.chen@gmail.com", "password124");
            }

        }
    }

    private void loadAllRepositories(){
        LoadStoresRepositoryUseCase.loadStoresRepository(this::onLoadRepoCacheComplete);
        LoadCategoriesRepositoryUseCase.loadCategoriesRepository(this::onLoadRepoCacheComplete);
        LoadUsersRepositoryUseCase.loadUsersRepository(this::onLoadRepoCacheComplete);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashViewModel =  new ViewModelProvider(this).get(SplashViewModel.class);
        setContentView(R.layout.activity_splash);
        
        loadAllRepositories();
    }
}