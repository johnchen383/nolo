package com.example.nolo.activities;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.nolo.R;
import com.example.nolo.interactors.category.LoadCategoriesRepositoryUseCase;
import com.example.nolo.interactors.item.LoadItemsRepositoryUseCase;
import com.example.nolo.interactors.store.LoadStoresRepositoryUseCase;
import com.example.nolo.interactors.user.GetCurrentUserUseCase;
import com.example.nolo.interactors.user.LoadUsersRepositoryUseCase;
import com.example.nolo.util.LocationUtil;
import com.example.nolo.util.ResponsiveView;
import com.example.nolo.viewmodels.ISplashViewModel;
import com.example.nolo.viewmodels.SplashViewModel;

import java.util.function.Consumer;

public class SplashActivity extends BaseActivity {
    private final int START_DELAY = 1000;
    private final int END_DELAY = 1000;
    private final int ANIMATION_INTERVAL = 1000;

    private ISplashViewModel splashViewModel;
    private ViewHolder vh;
    private ValueAnimator anim;


    private class ViewHolder {
        TextView load_state;
        View popupView;

        public ViewHolder() {
            load_state = findViewById(R.id.load_state);
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(LAYOUT_INFLATER_SERVICE);
            popupView = inflater.inflate(R.layout.popup_connectivity, null);
        }
    }

    private void navigate() {
        ActivityOptionsCompat fadeAnimOptions = ActivityOptionsCompat.makeCustomAnimation(this,
                android.R.anim.fade_in, android.R.anim.fade_out);

        if (GetCurrentUserUseCase.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class), fadeAnimOptions.toBundle());
        } else {
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
        LoadItemsRepositoryUseCase.loadItemsRepository(this::onLoadRepoCacheComplete);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        setContentView(R.layout.activity_splash);
        vh = new ViewHolder();

//        DataProvider.clearAndAddEntity(CollectionPath.laptops.name(), (a) -> {
//            DataProvider.clearAndAddEntity(CollectionPath.accessories.name(), (b) -> {
//                DataProvider.clearAndAddEntity(CollectionPath.phones.name(), (c) -> {
//                    DataProvider.addItemsToFirebase();
//                });
//            });
//        });

        checkLocationPermissionsAndContinue((a) -> pause(START_DELAY, (b) -> loadAllRepositories()));
    }
    
    private void checkLocationPermissionsAndContinue(Consumer<Void> func) {
        if (!LocationUtil.hasLocationPermissions(this)) {
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
        handler.postDelayed(() -> method.accept(null), delay);
    }

    private void setProgressLoad(float progress) {
        float oldProgress = ResponsiveView.getWeight(vh.load_state);
        float targetProgress = 1f - progress;

        anim = ValueAnimator.ofFloat(oldProgress, targetProgress);
        anim.addUpdateListener(valueAnimator -> {
            float val = (Float) valueAnimator.getAnimatedValue();
            ResponsiveView.setWeight(val, vh.load_state);
        });

        anim.setDuration(ANIMATION_INTERVAL);
        anim.start();
    }

    @Override
    public void onBackPressed() {
        return;
    }
}