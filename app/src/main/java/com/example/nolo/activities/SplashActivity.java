package com.example.nolo.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nolo.R;
import com.example.nolo.interactors.GetCategoriesUseCase;
import com.example.nolo.interactors.GetCurrentUserUseCase;
import com.example.nolo.interactors.LoadCategoriesRepositoryUseCase;
import com.example.nolo.interactors.LoadStoresRepositoryUseCase;
import com.example.nolo.interactors.LoadUsersRepositoryUseCase;
import com.example.nolo.interactors.LogInUseCase;
import com.example.nolo.interactors.LogOutUseCase;
import com.example.nolo.interactors.SignUpUseCase;
import com.example.nolo.util.Animation;
import com.example.nolo.util.LocationPermissions;
import com.example.nolo.viewmodels.CartViewModel;
import com.example.nolo.viewmodels.SplashViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.protobuf.Value;

import java.util.function.Consumer;

public class SplashActivity extends BaseActivity {
    private final int START_DELAY = 1000;
    private final int END_DELAY = 1000;
    private final int ANIMATION_INTERVAL = 1000;

    private SplashViewModel splashViewModel;
    private ViewHolder vh;
    private ValueAnimator anim;


    private class ViewHolder {
        TextView load_state;

        public ViewHolder() {
            load_state = findViewById(R.id.load_state);
        }
    }

    private void navigate() {
        ActivityOptionsCompat fadeAnimOptions = ActivityOptionsCompat.makeCustomAnimation(this,
                android.R.anim.fade_in, android.R.anim.fade_out);

        if (GetCurrentUserUseCase.getCurrentUser() != null) {
            //navigate to main if already signed in
            System.out.println("YAAAAA");
            startActivity(new Intent(this, MainActivity.class), fadeAnimOptions.toBundle());
        } else {
            //navigate to sign in if not signed in
            System.out.println("YOOOOOO");
            startActivity(new Intent(this, LogInActivity.class), fadeAnimOptions.toBundle());
        }

    }

    private void onLoadRepoCacheComplete(Class<?> repoClass) {
        splashViewModel.addLoaded(repoClass);
        setProgressLoad(splashViewModel.getLoadProgress());

        if (splashViewModel.getLoadable().equals(splashViewModel.getLoaded())) {
            System.out.println("All loaded");
            pause(END_DELAY, (a) -> navigate());
        }
    }

    private void loadAllRepositories() {
        LoadStoresRepositoryUseCase.loadStoresRepository(this::onLoadRepoCacheComplete);
        LoadCategoriesRepositoryUseCase.loadCategoriesRepository(this::onLoadRepoCacheComplete);
        LoadUsersRepositoryUseCase.loadUsersRepository(this::onLoadRepoCacheComplete);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        setContentView(R.layout.activity_splash);
        vh = new ViewHolder();

        checkLocationPermissionsAndContinue((a) -> pause(START_DELAY, (b) -> loadAllRepositories()));
    }

    private void checkLocationPermissionsAndContinue(Consumer<Void> func) {
        if (!LocationPermissions.hasLocationPermissions(this)) {
            promptLocationPermissionsDialog((a) -> func.accept(null));
        } else {
            func.accept(null);
        }
    }

    private void promptLocationPermissionsDialog(Consumer<Void> func) {
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(
                        new ActivityResultContracts.RequestMultiplePermissions(),
                        result -> func.accept(null));

        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    private void pause(int delay, Consumer<Void> method) {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            method.accept(null);
        }, delay);
    }

    private void setProgressLoad(float progress) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                vh.load_state.getLayoutParams();
        float oldProgress = params.weight;
        float targetProgress = 1f - progress;

        anim = ValueAnimator.ofFloat(oldProgress, targetProgress);
        anim.addUpdateListener(valueAnimator -> {
            float val = (Float) valueAnimator.getAnimatedValue();
            LinearLayout.LayoutParams newParams = (LinearLayout.LayoutParams) vh.load_state.getLayoutParams();
            newParams.weight = val;
            vh.load_state.setLayoutParams(newParams);
        });

        anim.setDuration(ANIMATION_INTERVAL);
        anim.start();
    }
}