package com.damskuy.petfeedermobileapp.ui.register;

import android.app.Application;
import android.content.Context;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.damskuy.petfeedermobileapp.R;
import com.damskuy.petfeedermobileapp.data.model.Result;
import com.damskuy.petfeedermobileapp.data.model.AuthenticatedUser;
import com.damskuy.petfeedermobileapp.data.auth.AuthRepository;

public class RegisterViewModel extends AndroidViewModel {

    private final MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private final MutableLiveData<Result<AuthenticatedUser>> registerResult = new MutableLiveData<>();
    private final AuthRepository authRepository;

    public RegisterViewModel(@NonNull Application application, AuthRepository authRepository) {
        super(application);
        this.authRepository = authRepository;
    }

    public LiveData<RegisterFormState> getRegisterFormState() { return registerFormState; }
    public LiveData<Result<AuthenticatedUser>> getRegisterResult() { return  registerResult; }

    public void register(String name, String email, String password) {
        authRepository.register(name, email, password, registerResult);
    }

    public void registerInputChanged(String name, String email, String password, String confPassword) {
        String nameError = validateName(name);
        String emailError = validateEmail(email);
        String passwordError = validatePassword(password);
        String confPasswordError = validateConfPassword(password, confPassword);
        registerFormState.setValue(new RegisterFormState(nameError, emailError, passwordError, confPasswordError));
    }

    @Nullable
    private String validateName(String name) {
        if (name.trim().isEmpty()) {
            return getString(R.string.validation_required);
        }
        if (name.length() > 12) {
            return getString(R.string.validation_input_exceeds, "Name", "12");
        }
        return null;
    }

    @Nullable
    private String validateEmail(String email) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return getString(R.string.validation_input_invalid, "email address");
        }
        if (email.length() > 255) {
            return getString(R.string.validation_input_exceeds, "Email", "255");
        }
        return null;
    }

    private String validatePassword(String password) {
        if (password.length() < 8) {
            return getString(R.string.validation_input_minimum, "Password", "8");
        }
        if (password.length() > 255) {
            return getString(R.string.validation_input_exceeds, "Password", "255");
        }
        return null;
    }

    private String validateConfPassword(String password, String confPassword) {
        if (!confPassword.equals(password)) {
            return  getString(R.string.validation_input_not_match, "Password");
        }
        return null;
    }

    private String getString(Integer resId) {
        return getApplication().getString(resId);
    }

    private String getString(Integer resId, Object... format) {
        return getApplication().getString(resId, format);
    }

    public boolean isLoggedIn() {
        Context ownerContext = getApplication().getApplicationContext();
        return this.authRepository.isAuthenticated(ownerContext);
    }
}
