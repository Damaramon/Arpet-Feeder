package com.damskuy.petfeedermobileapp.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.damskuy.petfeedermobileapp.data.auth.AuthRepository;

public class MainViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final AuthRepository authRepository;

    public MainViewModelFactory(Application application) {
        this.application = application;
        this.authRepository = AuthRepository.getInstance();
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(application, authRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
