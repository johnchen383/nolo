package com.example.nolo.activities;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.nolo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BaseActivity {
    private ViewHolder vh;

    private class ViewHolder {
        View navProfile;
        BottomNavigationView navView;

        public ViewHolder() {
            navProfile = findViewById(R.id.navigation_profile);
            navView = findViewById(R.id.nav_view);
            navView.setItemIconTintList(null);
        }
    }

    private void initListeners() {
//        vh.navProfile.setOnClickListener(v -> {
//            getSupportFragmentManager().beginTransaction().remove(AccountFragment.this).commit();
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vh = new ViewHolder();
        initListeners();

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_cart, R.id.navigation_profile)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        NavigationUI.setupWithNavController(vh.navView, navController);
    }

    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(R.anim.slide_down, R.anim.slide_up);
    }

    @Override
    public void onBackPressed() {
        Fragment profileAdditionFragment = getSupportFragmentManager().findFragmentByTag("PROFILE_ADDITION");
        if (profileAdditionFragment != null && profileAdditionFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().remove(profileAdditionFragment).commit();
        } else {
            return;
        }
    }
}
