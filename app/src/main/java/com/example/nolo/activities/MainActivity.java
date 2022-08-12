package com.example.nolo.activities;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends BaseActivity {
    private ViewHolder vh;

    private class ViewHolder {
        BottomNavigationView navView;

        public ViewHolder(){
            navView = findViewById(R.id.nav_view);
            navView.setItemIconTintList(null);
        }
    }

    // TODO: This is placeholder for after loaded repository class
    private void loadedRepository(Class<?> loadedRepository){
        System.out.println("Load finished");
    }

    /*
     * This method will load all repositories
     */
    private void loadAllRepositories() {
        LoadStoresRepositoryUseCase.loadStoresRepository(this::loadedRepository);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadAllRepositories();
//        DataProvider.addStoresToFirestore();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vh = new ViewHolder();

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_cart, R.id.navigation_profile)
                .build();
        
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        NavigationUI.setupWithNavController(vh.navView, navController);
    }

}