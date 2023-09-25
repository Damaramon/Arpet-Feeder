package com.damskuy.petfeedermobileapp.ui.login;

import androidx.annotation.Nullable;

public class LoginFormState {

    @Nullable
    private final String emailError;

    @Nullable
    private final String passwordError;

    public LoginFormState(
            @Nullable String emailError,
            @Nullable String passwordError
    ) {
        this.emailError = emailError;
        this.passwordError = passwordError;
    }

    @Nullable
    public String getEmailError() {
        return emailError;
    }

    public boolean isFormValid() { return emailError == null && passwordError == null; }
}
