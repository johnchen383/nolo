package com.example.nolo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.nolo.R;

import java.util.HashSet;
import java.util.Set;

public class SplashActivity extends BaseActivity {
    Set<Class<?>> loadable = new HashSet<>();
    Set<Class<?>> loaded = new HashSet<>();

    private void onLoadEntityComplete(Class<?> entity){
        loaded.add(entity);

        if (loadable.equals(loaded)){
            System.out.println("All loaded");
            ActivityOptionsCompat fadeAnimOptions = ActivityOptionsCompat.makeCustomAnimation(this,
                    android.R.anim.fade_in, android.R.anim.fade_out);

            startActivity(new Intent(this, MainActivity.class), fadeAnimOptions.toBundle());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadable.add(MainActivity.class);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            onLoadEntityComplete(MainActivity.class);
        }, 5000);
    }
}