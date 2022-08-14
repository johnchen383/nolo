package com.example.nolo.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.nolo.R;
import com.example.nolo.interactors.LoadCategoriesRepositoryUseCase;
import com.example.nolo.interactors.LoadItemsRepositoryUseCase;
import com.example.nolo.interactors.LoadStoresRepositoryUseCase;
import com.example.nolo.interactors.SignUpUseCase;
import com.example.nolo.viewmodels.SplashViewModel;

public class SplashActivity extends BaseActivity {
    private SplashViewModel splashViewModel;

    private void onLoadRepoCacheComplete(Class<?> repoClass){
        splashViewModel.addLoaded(repoClass);

        if (splashViewModel.getLoadable().equals(splashViewModel.getLoaded())){
            System.out.println("All loaded");
            ActivityOptionsCompat fadeAnimOptions = ActivityOptionsCompat.makeCustomAnimation(this,
                    android.R.anim.fade_in, android.R.anim.fade_out);

            startActivity(new Intent(this, MainActivity.class), fadeAnimOptions.toBundle());
        }
    }

    private void loadAllRepositories(){
        LoadStoresRepositoryUseCase.loadStoresRepository(this::onLoadRepoCacheComplete);
        LoadCategoriesRepositoryUseCase.loadCategoriesRepository(this::onLoadRepoCacheComplete);
        LoadItemsRepositoryUseCase.loadItemsRepository(this::onLoadRepoCacheComplete);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashViewModel =  new ViewModelProvider(this).get(SplashViewModel.class);
        setContentView(R.layout.activity_splash);
        
        loadAllRepositories();

        SignUpUseCase.signUp("test@gmail.com", "testTest");
    }
}