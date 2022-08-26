package com.example.nolo.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.ViewModelProvider;

import com.example.nolo.R;
import com.example.nolo.util.Animation;
import com.example.nolo.viewmodels.LogInViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class LogInActivity extends BaseActivity {
    private LogInViewModel logInViewModel;

    private ViewHolder vh;

    private class ViewHolder {
        EditText emailInput, passwordInput;
        TextInputLayout emailLayout, passwordLayout;
        RelativeLayout eyeBtn, signUpBtn;
        ImageView eyeIcon;
        TextView forgotPassword, signUpText, errorText;
        MaterialButton logIn;

        public ViewHolder() {
            emailInput = findViewById(R.id.email_edit);
            emailLayout = findViewById(R.id.email_layout);
            passwordInput = findViewById(R.id.password_edit);
            passwordLayout = findViewById(R.id.password_layout);
            eyeBtn = findViewById(R.id.eye_btn);
            eyeIcon = findViewById(R.id.eye_icon);
            forgotPassword = findViewById(R.id.forgot_text_view);
            logIn = findViewById(R.id.login_button);
//            logInGoogle = findViewById(R.id.login_google_button);
            signUpBtn = findViewById(R.id.register_btn);
            signUpText = findViewById(R.id.register_text);
            errorText = findViewById(R.id.error_text_view);
        }
    }

    private void initStyling() {
        vh.passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        vh.signUpText.setPaintFlags(vh.signUpText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        vh.emailLayout.setBoxStrokeColorStateList(AppCompatResources.getColorStateList(this, R.color.text_input_layout_stroke_colour));
        vh.passwordLayout.setBoxStrokeColorStateList(AppCompatResources.getColorStateList(this, R.color.text_input_layout_stroke_colour));
    }

    private void logIn() {
        String userEmail = vh.emailInput.getText().toString();
        String userPassword = vh.passwordInput.getText().toString();

        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            return;
        }

        logInViewModel.logIn((error) -> {
            if (error == null) {
                startActivity(new Intent(this, MainActivity.class), Animation.Fade(this).toBundle());
            } else {
                vh.errorText.setText(error);
                vh.errorText.setVisibility(View.VISIBLE);
            }
        }, userEmail, userPassword);
    }

    private void initListeners() {
        vh.eyeBtn.setOnClickListener(v -> {
            boolean isHidden = vh.passwordInput.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            togglePassword(isHidden);
        });

        vh.forgotPassword.setOnClickListener(v -> {
//            startActivity(new Intent(this, ForgotPasswordActivity.class), Animation.Fade(this).toBundle());
            Toast.makeText(this, "Password Reset!", Toast.LENGTH_SHORT).show();
        });

        vh.signUpBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class));
            overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_stationery);
        });

        vh.logIn.setOnClickListener(v -> {
            hideKeyboard(v, true);
            logIn();
        });

//        vh.logInGoogle.setOnClickListener(v -> {
//            hideKeyboard(v, true);
//        });

        vh.emailInput.setOnFocusChangeListener((v, hasFocus) -> {
            hideKeyboard(v, false);
        });

        vh.passwordInput.setOnFocusChangeListener((v, hasFocus) -> {
            hideKeyboard(v, false);
        });

        vh.passwordInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(v, true);
                logIn();
                return true;
            }
            return false;
        });
    }

    public void togglePassword(boolean isHidden) {
        vh.passwordInput.setInputType(isHidden ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        vh.eyeIcon.setImageResource(isHidden ? R.drawable.signin_icon_eye_closed : R.drawable.signin_icon_eye_open);
        setCursorToEnd();
    }

    public void clearFocus() {
        vh.emailInput.clearFocus();
        vh.passwordInput.clearFocus();
    }

    public void hideKeyboard(View view, Boolean isClearFocus) {
        if (isClearFocus) {
            clearFocus();
        }
        if (!vh.emailInput.hasFocus() && !vh.passwordInput.hasFocus()) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void setCursorToEnd() {
        CharSequence charSeq = vh.passwordInput.getText();
        Spannable spanText = (Spannable) charSeq;
        Selection.setSelection(spanText, charSeq.length());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logInViewModel = new ViewModelProvider(this).get(LogInViewModel.class);
        setContentView(R.layout.activity_login);
        vh = new ViewHolder();
        initStyling();
        initListeners();

    }

    @Override
    public void onBackPressed() {
        return;
    }
}
