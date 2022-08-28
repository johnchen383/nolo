package com.example.nolo.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nolo.R;

/**
 * It does not violate LSP.
 * It is justified in the report, please check.
 */
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