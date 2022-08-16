package com.example.nolo.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.nolo.R;
import com.example.nolo.util.Animation;
import com.example.nolo.viewmodels.LogInViewModel;
import com.example.nolo.viewmodels.SplashViewModel;
import com.google.android.material.button.MaterialButton;

public class LogInActivity extends BaseActivity {
    private LogInViewModel logInViewModel;

    private ViewHolder vh;

    private class ViewHolder {
        EditText emailInput;
        EditText passwordInput;
        ImageView eyeBtn;
        TextView forgotPassword;
        TextView signUp;
        MaterialButton logIn;
        MaterialButton logInGoogle;


        public ViewHolder(){
            emailInput = findViewById(R.id.email_edit);
            passwordInput = findViewById(R.id.password_edit);
            eyeBtn = findViewById(R.id.eye_open);
            forgotPassword = findViewById(R.id.forgot_text_view);
            logIn = findViewById(R.id.login_button);
            logInGoogle = findViewById(R.id.login_google_button);
            signUp = findViewById(R.id.register_text_view);
        }
    }

    private void initStyling() {
        vh.passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        vh.signUp.setPaintFlags(vh.signUp.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
    }

    private void initListeners() {
        vh.eyeBtn.setOnClickListener(v -> {
            boolean isHidden = vh.passwordInput.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            System.out.println(vh.passwordInput.getInputType());
            if (isHidden) {
                vh.passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                vh.eyeBtn.setImageResource(R.drawable.login_icon_eye_closed);
            } else {
                vh.passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                vh.eyeBtn.setImageResource(R.drawable.login_icon_eye_open);
            }
        });

        vh.forgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(this, ForgotPasswordActivity.class), Animation.Fade(this).toBundle());
        });

        vh.signUp.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class), Animation.Fade(this).toBundle());
        });

        vh.logIn.setOnClickListener(v -> {
            String userEmail = vh.emailInput.getText().toString();
            String userPassword = vh.passwordInput.getText().toString();

            System.out.println("Email: " + userEmail + "\nPassword: " + userPassword);

            logInViewModel.logIn((error) -> {
                if (error == null) {
                    startActivity(new Intent(this, MainActivity.class), Animation.Fade(this).toBundle());
                } else {
                    //display error message
                    System.out.println("ERR: " + error);
                }
            }, userEmail, userPassword);
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logInViewModel =  new ViewModelProvider(this).get(LogInViewModel.class);
        setContentView(R.layout.activity_login);
        vh = new ViewHolder();
        initStyling();
        initListeners();

    }

}
