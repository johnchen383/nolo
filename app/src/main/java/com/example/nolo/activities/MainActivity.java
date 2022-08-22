package com.example.nolo.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.nolo.R;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.interactors.item.GetCategoryItemsUseCase;
import com.example.nolo.dataprovider.DataProvider;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.enums.CollectionPath;
import com.example.nolo.interactors.item.GetCategoryItemsUseCase;
import com.example.nolo.util.DeviceLocation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends BaseActivity {
    private ViewHolder vh;

    private class ViewHolder {
        BottomNavigationView navView;

        public ViewHolder(){
            navView = findViewById(R.id.nav_view);
            navView.setItemIconTintList(null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vh = new ViewHolder();

//        List<IItem> items = GetCategoryItemsUseCase.getCategoryItems(CategoryType.phones);
//        vh.navView.setBackground(new ColorDrawable(0));

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
        return;
    }
}
