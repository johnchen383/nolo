package com.example.nolo.viewmodels;

import java.util.function.Consumer;

public interface IChangePasswordViewModel {
    void changePassword(Consumer<String> onUserChangePassword, String email, String password);
}
