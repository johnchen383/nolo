package com.example.nolo.activities;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.example.nolo.R;
import com.example.nolo.interactors.SignUpUseCase;
import com.example.nolo.viewmodels.LogInViewModel;
import com.example.nolo.viewmodels.SplashViewModel;

public class LogInActivity extends BaseActivity {
    private LogInViewModel logInViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
