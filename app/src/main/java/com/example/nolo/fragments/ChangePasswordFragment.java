package com.example.nolo.fragments;

import android.os.Bundle;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.nolo.R;
import com.example.nolo.activities.MainActivity;
import com.example.nolo.util.FragmentUtil;
import com.example.nolo.util.Keyboard;
import com.example.nolo.viewmodels.ChangePasswordViewModel;
import com.example.nolo.viewmodels.IChangePasswordViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;

public class ChangePasswordFragment extends Fragment {
    private IChangePasswordViewModel changePasswordViewModel;
    private ViewHolder vh;

    private class ViewHolder {
        EditText oldPasswordInput, newPasswordInput, repeatPasswordInput;
        TextInputLayout oldPasswordLayout, newPasswordLayout, repeatPasswordLayout;
        RelativeLayout oldEyeBtn, newEyeBtn, repeatEyeBtn, backBtn;
        ImageView oldEyeIcon, newEyeIcon, repeatEyeIcon;
        TextView errorText;
        MaterialButton saveBtn;
        MaterialCardView successMsg;

        public ViewHolder(View view) {
            oldPasswordInput = view.findViewById(R.id.old_password_edit);
            oldPasswordLayout = view.findViewById(R.id.old_password_layout);
            newPasswordInput = view.findViewById(R.id.new_password_edit);
            newPasswordLayout = view.findViewById(R.id.new_password_layout);
            repeatPasswordInput = view.findViewById(R.id.repeat_password_edit);
            repeatPasswordLayout = view.findViewById(R.id.repeat_password_layout);
            oldEyeBtn = view.findViewById(R.id.old_eye_btn);
            oldEyeIcon = view.findViewById(R.id.old_eye_icon);
            newEyeBtn = view.findViewById(R.id.new_eye_btn);
            newEyeIcon = view.findViewById(R.id.new_eye_icon);
            repeatEyeBtn = view.findViewById(R.id.repeat_eye_btn);
            repeatEyeIcon = view.findViewById(R.id.repeat_eye_icon);
            errorText = view.findViewById(R.id.error_text_view);
            saveBtn = view.findViewById(R.id.save_button);
            backBtn = view.findViewById(R.id.back_btn);
            successMsg = view.findViewById(R.id.success_message);
        }
    }

    public ChangePasswordFragment() {
        super(R.layout.fragment_change_password);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        changePasswordViewModel =
                new ViewModelProvider(this).get(ChangePasswordViewModel.class);
        vh = new ViewHolder(view);

        // Initialisation
        init();
        ((MainActivity) getActivity()).updateCartBadge();
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    /**
     * Initialisation
     */
    private void init() {
        initStyling();
        initListeners();
    }

    private void initStyling() {
        vh.oldPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        vh.newPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        vh.repeatPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        vh.oldPasswordLayout.setBoxStrokeColorStateList(AppCompatResources.getColorStateList(getActivity(), R.color.text_input_layout_stroke_colour));
        vh.newPasswordLayout.setBoxStrokeColorStateList(AppCompatResources.getColorStateList(getActivity(), R.color.text_input_layout_stroke_colour));
        vh.newPasswordLayout.setBoxStrokeColorStateList(AppCompatResources.getColorStateList(getActivity(), R.color.text_input_layout_stroke_colour));

        getActivity().getWindow().setStatusBarColor(getActivity().getColor(R.color.navy));
    }

    private void initListeners() {
        vh.backBtn.setOnClickListener(v -> {
            FragmentUtil.popFragment(getActivity(), ChangePasswordFragment.class.getName());
        });

        vh.oldEyeBtn.setOnClickListener(v -> {
            togglePassword(vh.oldPasswordInput, vh.oldEyeIcon);
        });

        vh.newEyeBtn.setOnClickListener(v -> {
            togglePassword(vh.newPasswordInput, vh.newEyeIcon);
        });

        vh.repeatEyeBtn.setOnClickListener(v -> {
            togglePassword(vh.repeatPasswordInput, vh.repeatEyeIcon);
        });

        vh.saveBtn.setOnClickListener(v -> {
            hideKeyboard(v, true);
            changePassword();
        });

        vh.oldPasswordInput.setOnFocusChangeListener((v, hasFocus) -> {
            hideKeyboard(v, false);
            if (hasFocus) {
                toggleMessageVisibility(false);
            }
        });

        vh.newPasswordInput.setOnFocusChangeListener((v, hasFocus) -> {
            hideKeyboard(v, false);
            if (hasFocus) {
                toggleMessageVisibility(false);
            }
        });

        vh.repeatPasswordInput.setOnFocusChangeListener((v, hasFocus) -> {
            hideKeyboard(v, false);
            if (hasFocus) {
                toggleMessageVisibility(false);
            }
        });

        vh.repeatPasswordInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(v, true);
                changePassword();
                return true;
            }
            return false;
        });
    }

    /**
     * Show/Hide password
     *
     * @param input EditText view
     * @param icon ImageView
     */
    private void togglePassword(EditText input, ImageView icon) {
        boolean isHidden = input.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setInputType(isHidden ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        icon.setImageResource(isHidden ? R.drawable.signin_icon_eye_closed : R.drawable.signin_icon_eye_open);
        setCursorToEnd(input);
    }

    /**
     * Check validation & Change password
     */
    private void changePassword() {
        String oldPassword = vh.oldPasswordInput.getText().toString();
        String newPassword = vh.newPasswordInput.getText().toString();
        String repeatPassword = vh.repeatPasswordInput.getText().toString();

        if (oldPassword.isEmpty()) {
            vh.errorText.setText("Please enter your current password.");
            vh.errorText.setVisibility(View.VISIBLE);
            return;
        } else if (newPassword.isEmpty() && repeatPassword.isEmpty()) {
            vh.errorText.setText("Please enter a password.");
            vh.errorText.setVisibility(View.VISIBLE);
            return;
        } else if (!newPassword.equals(repeatPassword)) {
            vh.errorText.setText("The passwords do not match.");
            vh.errorText.setVisibility(View.VISIBLE);
            return;
        }

        changePasswordViewModel.changePassword((error) -> {
            if (error == null) {
                vh.errorText.setVisibility(View.INVISIBLE);
                toggleMessageVisibility(true);
            } else {
                vh.errorText.setText(error);
                vh.errorText.setVisibility(View.VISIBLE);
            }
        }, oldPassword, newPassword);
    }

    /**
     * Clear focus on three EditText views
     */
    private void clearFocus() {
        vh.oldPasswordInput.clearFocus();
        vh.newPasswordInput.clearFocus();
        vh.repeatPasswordInput.clearFocus();
    }

    /**
     * Hide keyboard if no focus on any of the EditText views
     *
     * @param view current view
     * @param isClearFocus identify whether to clear focus on the EditText views
     */
    private void hideKeyboard(View view, boolean isClearFocus) {
        if (isClearFocus) {
            clearFocus();
        }
        if (!vh.oldPasswordInput.hasFocus() && !vh.newPasswordInput.hasFocus() && !vh.repeatPasswordInput.hasFocus()) {
            Keyboard.hide(getActivity(), view);
        }
    }

    /**
     * Set cursor to the end of the EditText view
     *
     * @param input EditText view
     */
    private void setCursorToEnd(EditText input) {
        CharSequence charSeq = input.getText();
        Spannable spanText = (Spannable) charSeq;
        Selection.setSelection(spanText, charSeq.length());
    }

    /**
     * Show/Hide success message or save button
     *
     * @param isVisible Boolean for visibility;
     *                  True for success message;
     *                  False for save button
     */
    private void toggleMessageVisibility(boolean isVisible) {
        vh.successMsg.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        vh.saveBtn.setVisibility(isVisible ? View.GONE : View.VISIBLE);
    }
}