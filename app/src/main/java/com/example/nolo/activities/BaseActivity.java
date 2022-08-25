package com.example.nolo.activities;

import static androidx.core.view.WindowCompat.getInsetsController;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.nolo.R;

//TODO: justify that it does not violate LSP
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide title bar and set status bar to navy
        getWindow().setStatusBarColor(getColor(R.color.navy));
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //lock portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}