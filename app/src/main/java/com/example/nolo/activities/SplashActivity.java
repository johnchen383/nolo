package com.example.nolo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.nolo.R;
import com.example.nolo.interactors.SignUpUseCase;
import com.example.nolo.viewmodels.CartViewModel;
import com.example.nolo.viewmodels.SplashViewModel;

import java.util.HashSet;
import java.util.Set;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashViewModel =  new ViewModelProvider(this).get(SplashViewModel.class);
        setContentView(R.layout.activity_splash);

        SignUpUseCase.signUp("test@gmail.com", "testTest");

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            onLoadRepoCacheComplete(MainActivity.class);
        }, 5000);
    }
}