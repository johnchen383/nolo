package com.example.nolo.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.nolo.R;
import com.example.nolo.dataprovider.DataProvider;
import com.example.nolo.repositories.store.StoresRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ViewHolder vh;

    private class ViewHolder {
        BottomNavigationView navView;

        public ViewHolder(){
            navView = findViewById(R.id.nav_view);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        StoresRepository.getInstance().fetchStoreById("LcJHoRqrS05bzTUxTDZd");
//        DataProvider.addStoresToFirestore();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vh = new ViewHolder();

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_cart, R.id.navigation_profile)
                .build();
        
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        NavigationUI.setupWithNavController(vh.navView, navController);
    }

}