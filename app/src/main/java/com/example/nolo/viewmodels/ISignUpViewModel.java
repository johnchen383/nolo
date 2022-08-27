package com.example.nolo.viewmodels;

import java.util.function.Consumer;

public interface ISignUpViewModel {
    void signUp(Consumer<String> onUserSignedUp, String email, String password);
}
