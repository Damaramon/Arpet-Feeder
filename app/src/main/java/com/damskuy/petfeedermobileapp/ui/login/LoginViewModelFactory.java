package com.damskuy.petfeedermobileapp.ui.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.damskuy.petfeedermobileapp.data.auth.AuthRepository;

public class LoginViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;

    public LoginViewModelFactory(Application application)  {
        this.application = application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(
                    application,
                    AuthRepository.getInstance()
            );
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
