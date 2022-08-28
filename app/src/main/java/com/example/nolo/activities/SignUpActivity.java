package com.example.nolo.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.ViewModelProvider;

import com.example.nolo.R;
import com.example.nolo.util.Animation;
import com.example.nolo.viewmodels.SignUpViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends BaseActivity {
    private SignUpViewModel signUpViewModel;
    private ViewHolder vh;

    private class ViewHolder {
        EditText emailInput, passwordInput, repeatPasswordInput;
        TextInputLayout emailLayout, passwordLayout, repeatPasswordLayout;
        RelativeLayout eyeBtn, repeatEyeBtn, logInBtn, checkboxBtn, termsBtn, privacyBtn;
        CheckBox checkbox;
        ImageView eyeIcon, repeatEyeIcon;
        TextView logInText, termsText, privacyText, errorText;
        MaterialButton signUp;

        public ViewHolder() {
            emailInput = findViewById(R.id.email_edit);
            emailLayout = findViewById(R.id.email_layout);
            passwordInput = findViewById(R.id.password_edit);
            passwordLayout = findViewById(R.id.password_layout);
            repeatPasswordInput = findViewById(R.id.repeat_password_edit);
            repeatPasswordLayout = findViewById(R.id.repeat_password_layout);
            eyeBtn = findViewById(R.id.eye_btn);
            eyeIcon = findViewById(R.id.eye_icon);
            repeatEyeBtn = findViewById(R.id.repeat_eye_btn);
            repeatEyeIcon = findViewById(R.id.repeat_eye_icon);
            checkboxBtn = findViewById(R.id.checkbox_btn);
            checkbox = findViewById(R.id.checkbox);
            termsBtn = findViewById(R.id.terms_btn);
            termsText = findViewById(R.id.terms_text_view);
            privacyBtn = findViewById(R.id.privacy_btn);
            privacyText = findViewById(R.id.privacy_text_view);
            signUp = findViewById(R.id.signup_button);
            logInBtn = findViewById(R.id.login_btn);
            logInText = findViewById(R.id.login_text);
            errorText = findViewById(R.id.error_text_view);
        }
    }

    private void initStyling() {
        vh.passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        vh.repeatPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        vh.termsText.setPaintFlags(vh.termsText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        vh.privacyText.setPaintFlags(vh.privacyText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        vh.logInText.setPaintFlags(vh.logInText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        vh.emailLayout.setBoxStrokeColorStateList(AppCompatResources.getColorStateList(this, R.color.text_input_layout_stroke_colour));
        vh.passwordLayout.setBoxStrokeColorStateList(AppCompatResources.getColorStateList(this, R.color.text_input_layout_stroke_colour));
    }

    private void signUp() {
        String userEmail = vh.emailInput.getText().toString();
        String userPassword = vh.passwordInput.getText().toString();
        String userRepeatPassword = vh.repeatPasswordInput.getText().toString();

        if (userEmail.isEmpty()) {
            vh.errorText.setText("Please enter an email address.");
            vh.errorText.setVisibility(View.VISIBLE);
            return;
        } else if (userPassword.isEmpty() && userRepeatPassword.isEmpty()) {
            vh.errorText.setText("Please enter a password.");
            vh.errorText.setVisibility(View.VISIBLE);
            return;
        } else if (!userPassword.equals(userRepeatPassword)) {
            vh.errorText.setText("The passwords do not match.");
            vh.errorText.setVisibility(View.VISIBLE);
            return;
        } else if (!vh.checkbox.isChecked()) {
            vh.errorText.setText("Please read and accept the Terms of Use and Privacy Policy.");
            vh.errorText.setVisibility(View.VISIBLE);
            return;
        }

        signUpViewModel.signUp((error) -> {
            if (error == null) {
                startActivity(new Intent(this, MainActivity.class).putExtra("MessageFromSignUpActivity", "New account created successfully."), Animation.Fade(this).toBundle());
            } else {
                vh.errorText.setText(error);
                vh.errorText.setVisibility(View.VISIBLE);
            }
        }, userEmail, userPassword);
    }

    private void initListeners() {
        vh.eyeBtn.setOnClickListener(v -> {
            togglePassword(vh.passwordInput, vh.eyeIcon);
        });

        vh.repeatEyeBtn.setOnClickListener(v -> {
            togglePassword(vh.repeatPasswordInput, vh.repeatEyeIcon);
        });

        vh.checkboxBtn.setOnClickListener(v -> {
            vh.checkbox.setChecked(!vh.checkbox.isChecked());
        });

        vh.termsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("https://nolo-docs.vercel.app/nolo_terms.pdf"), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        });

        vh.privacyBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("https://nolo-docs.vercel.app/nolo_privacy.pdf"), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        });

        vh.logInBtn.setOnClickListener(v -> {
//            startActivity(new Intent(this, LogInActivity.class));
            finish();
        });

        vh.signUp.setOnClickListener(v -> {
            hideKeyboard(v, true);
            signUp();
        });

        vh.emailInput.setOnFocusChangeListener((v, hasFocus) -> {
            hideKeyboard(v, false);
        });

        vh.passwordInput.setOnFocusChangeListener((v, hasFocus) -> {
            hideKeyboard(v, false);
        });

        vh.repeatPasswordInput.setOnFocusChangeListener((v, hasFocus) -> {
            hideKeyboard(v, false);
        });

        vh.repeatPasswordInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(v, true);
                signUp();
                return true;
            }
            return false;
        });
    }

    public void togglePassword(EditText input, ImageView icon) {
        boolean isHidden = input.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setInputType(isHidden ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        icon.setImageResource(isHidden ? R.drawable.signin_icon_eye_closed : R.drawable.signin_icon_eye_open);
        setCursorToEnd(input);
    }

    public void clearFocus() {
        vh.emailInput.clearFocus();
        vh.passwordInput.clearFocus();
        vh.repeatPasswordInput.clearFocus();
    }

    public void hideKeyboard(View view, Boolean isClearFocus) {
        if (isClearFocus) {
            clearFocus();
        }
        if (!vh.emailInput.hasFocus() && !vh.passwordInput.hasFocus() && !vh.repeatPasswordInput.hasFocus()) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void setCursorToEnd(EditText input) {
        CharSequence charSeq = input.getText();
        Spannable spanText = (Spannable) charSeq;
        Selection.setSelection(spanText, charSeq.length());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        setContentView(R.layout.activity_signup);
        vh = new ViewHolder();
        initStyling();
        initListeners();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_stationery, R.anim.slide_out_to_right);
    }
}
