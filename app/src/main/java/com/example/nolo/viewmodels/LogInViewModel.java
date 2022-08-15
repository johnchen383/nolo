package com.example.nolo.viewmodels;

import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.ViewModel;

import android.content.Intent;

import com.example.nolo.activities.LogInActivity;
import com.example.nolo.activities.MainActivity;
import com.example.nolo.interactors.LogInUseCase;
import com.example.nolo.util.Animation;

import java.util.function.Consumer;


public class LogInViewModel extends ViewModel {

     public void logIn(Consumer<String> userLoggedIn, String email, String password) {
         LogInUseCase.logIn(userLoggedIn, email, password);
     }
}
