package com.damskuy.petfeedermobileapp.ui.devices;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DevicesViewModelFactory implements ViewModelProvider.Factory {

    private final Context activityContext;
    private final BluetoothManager bluetoothManager;

    public DevicesViewModelFactory(Context activityContext, BluetoothManager bluetoothManager) {
        this.activityContext = activityContext;
        this.bluetoothManager = bluetoothManager;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DevicesViewModel.class)) {
            return (T) new DevicesViewModel(activityContext, bluetoothManager);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
