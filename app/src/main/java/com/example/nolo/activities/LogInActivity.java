package com.example.nolo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityOptionsCompat;

import com.example.nolo.R;
import com.example.nolo.viewmodels.LogInViewModel;

public class LogInActivity extends BaseActivity {
    private LogInViewModel logInViewModel;

    private ViewHolder vh;

    private class ViewHolder {
        EditText passwordInput;
        ImageView eyeBtn;
        TextView forgotPassword;
        TextView signUp;
        Button logIn;
        Button logInGoogle;


        public ViewHolder(){
            passwordInput = findViewById(R.id.password_edit);
            eyeBtn = findViewById(R.id.eye_open);
            forgotPassword = findViewById(R.id.forgot_text_view);
            logIn = findViewById(R.id.login_button);
            logInGoogle = findViewById(R.id.login_google_button);
            signUp = findViewById(R.id.register_text_view);
        }
    }

    private void initListeners() {
        ActivityOptionsCompat fadeAnimOptions = ActivityOptionsCompat.makeCustomAnimation(this,
                android.R.anim.fade_in, android.R.anim.fade_out);

        vh.passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

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
            startActivity(new Intent(this, ForgotPasswordActivity.class), fadeAnimOptions.toBundle());
        });

        vh.signUp.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class), fadeAnimOptions.toBundle());
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
