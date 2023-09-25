package com.damskuy.petfeedermobileapp.ui.register;

import androidx.annotation.Nullable;

public class RegisterFormState {

    @Nullable
    private final String nameError;

    @Nullable
    private final String emailError;

    @Nullable
    private final String passwordError;

    @Nullable
    private final String confPasswordError;

    public RegisterFormState(
            @Nullable String nameError,
            @Nullable String emailError,
            @Nullable String passwordError,
            @Nullable String confPasswordError
    ) {
        this.nameError = nameError;
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.confPasswordError = confPasswordError;
    }

    @Nullable
    public String getNameError() { return nameError; }

    @Nullable
    public String getEmailError() {
        return emailError;
    }

    @Nullable
    public String getPasswordError() {
        return passwordError;
    }

    @Nullable
    public String getConfPasswordError() {
        return confPasswordError;
    }

    public boolean isFormValid() {
        return nameError == null &&
                emailError == null &&
                passwordError == null &&
                confPasswordError == null;
    }
}
