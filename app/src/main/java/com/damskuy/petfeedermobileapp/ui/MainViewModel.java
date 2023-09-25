package com.damskuy.petfeedermobileapp.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.damskuy.petfeedermobileapp.data.auth.AuthRepository;

public class MainViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;
    private final MutableLiveData<MainFragment> activeFragment = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application, AuthRepository authRepository) {
        super(application);
        this.authRepository = authRepository;
    }

    public void changeFragment(int fragmentIconId) {
        activeFragment.postValue(new MainFragment(fragmentIconId));
    }

    public void logout() {
        authRepository.logout(getApplication().getApplicationContext());
    }

    public LiveData<MainFragment> getActiveFragment() {
       return this.activeFragment;
    }
}
