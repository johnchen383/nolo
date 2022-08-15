package com.example.nolo.activities;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.lifecycle.ViewModelProvider;

import com.example.nolo.R;
import com.example.nolo.interactors.SignUpUseCase;
import com.example.nolo.viewmodels.LogInViewModel;
import com.example.nolo.viewmodels.SplashViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LogInActivity extends BaseActivity {
    private LogInViewModel logInViewModel;

    private ViewHolder vh;

    private class ViewHolder {
        EditText passwordInput;
        ImageView eyeBtn;

        public ViewHolder(){
            passwordInput = findViewById(R.id.password_edit);
            eyeBtn = findViewById(R.id.eye_open);
        }
    }

    private void initListeners() {

        vh.passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        vh.eyeBtn.setOnClickListener(v -> {
            boolean isHidden = vh.passwordInput.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            System.out.println(vh.passwordInput.getInputType());
            if (isHidden) {
                vh.passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                vh.passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        vh = new ViewHolder();
        initListeners();

    }

}
