package com.damskuy.petfeedermobileapp.ui.login;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.damskuy.petfeedermobileapp.R;
import com.damskuy.petfeedermobileapp.data.model.Result;
import com.damskuy.petfeedermobileapp.data.auth.AuthRepository;
import com.damskuy.petfeedermobileapp.data.model.AuthenticatedUser;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class LoginViewModel extends AndroidViewModel {

    private final MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private final MutableLiveData<Result<AuthenticatedUser>> loginResult = new MutableLiveData<>();
    private final AuthRepository authRepository;

    public LoginViewModel(@NonNull Application application, AuthRepository authRepository) {
        super(application);
        this.authRepository = authRepository;
    }

    public LiveData<LoginFormState> getLoginFormState() { return loginFormState; }

    public LiveData<Result<AuthenticatedUser>> getLoginResult() { return loginResult; }

    public void login(String email, String password) {
        authRepository.login(email, password, loginResult);
    }

    public void loginWithGoogle(GoogleSignInAccount googleAccount) {
        authRepository.loginWithGoogle(googleAccount, loginResult);
    }

    public void loginInputChange(String email, String password) {
        String emailError = validateEmail(email);
        String passwordError = validatePassword(password);
        loginFormState.setValue(new LoginFormState(emailError, passwordError));
    }

    @Nullable
    private String validateEmail(String email) {
        if (email.trim().isEmpty()) { return getString(R.string.validation_required); }
        return null;
    }

    private String validatePassword(String password) {
        if (password.trim().isEmpty()) { return getString(R.string.validation_required); }
        return null;
    }

    private String getString(Integer resId) {
        return getApplication().getString(resId);
    }

    public boolean isLoggedIn() {
        Context ownerContext = getApplication().getApplicationContext();
        return this.authRepository.isAuthenticated(ownerContext);
    }
}
