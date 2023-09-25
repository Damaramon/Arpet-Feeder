package com.damskuy.petfeedermobileapp.ui.register;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.damskuy.petfeedermobileapp.data.auth.AuthRepository;

public class RegisterViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;

    public RegisterViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RegisterViewModel.class)) {
            return (T) new RegisterViewModel(
                    application,
                    AuthRepository.getInstance()
            );
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
