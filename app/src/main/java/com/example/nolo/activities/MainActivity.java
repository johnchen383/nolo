package com.example.nolo.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.nolo.R;
import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.interactors.user.GetCartItemsUseCase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

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
    
    private void displaySignUpToast() {
        String toastText = getIntent().getStringExtra("SignUpMessage");
        if (toastText == null) {
            return;
        }

        Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartBadge();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displaySignUpToast();

        vh = new ViewHolder();
        initListeners();

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_cart, R.id.navigation_profile)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        NavigationUI.setupWithNavController(vh.navView, navController);

        updateCartBadge();
    }

    public void updateCartBadge(){
        List<Purchasable> cartItems = GetCartItemsUseCase.getCartItems();
        int sum = 0;

        for (Purchasable c : cartItems){
            sum += c.getQuantity();
        }

        if (vh == null) return;

        if (sum == 0){
            vh.navView.removeBadge(R.id.navigation_cart);
            return;
        }

        vh.navView.getOrCreateBadge(R.id.navigation_cart).setNumber(sum);
        vh.navView.getOrCreateBadge(R.id.navigation_cart).setBackgroundColor(getColor(R.color.white));
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
