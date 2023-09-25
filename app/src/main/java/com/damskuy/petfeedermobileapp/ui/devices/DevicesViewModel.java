package com.damskuy.petfeedermobileapp.ui.devices;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.damskuy.petfeedermobileapp.data.model.Result;
import com.damskuy.petfeedermobileapp.data.model.ScannedDevice;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class DevicesViewModel extends ViewModel {

    private final Context context;
    private final BluetoothAdapter bluetoothAdapter;
    private final MutableLiveData<Boolean> isBluetoothOn = new MutableLiveData<>();
    private final MutableLiveData<Result<Boolean>> bluetoothAvailability = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ScannedDevice>> scannedDevices = new MutableLiveData<>();
    private final HashMap<String, String> scannedDevicesMap = new HashMap<>();

    public DevicesViewModel(Context context, BluetoothManager bluetoothManager) {
        this.context = context;
        bluetoothAdapter = bluetoothManager.getAdapter();
    }

    public LiveData<Boolean> getIsBluetoothOn() {
        return isBluetoothOn;
    }

    public LiveData<Result<Boolean>> getBluetoothAvailability() {
        return bluetoothAvailability;
    }

    public LiveData<ArrayList<ScannedDevice>> getScannedDevices() {
        return scannedDevices;
    }

    public void checkBluetoothConnection() {
        if (bluetoothAdapter == null) isBluetoothOn.postValue(false);
        else isBluetoothOn.postValue(bluetoothAdapter.isEnabled());
    }

    public void attemptConnectToBluetooth(ActivityResultLauncher<Intent> resultLauncher) {
        if (bluetoothAdapter == null) {
            Exception error = new Exception("Device doesn't support bluetooth connection");
            bluetoothAvailability.postValue(new Result.Error<>(error));
            return;
        }
        turnOnBluetooth(resultLauncher);
    }

    public void turnOnBluetooth(ActivityResultLauncher<Intent> resultLauncher) {
        if (!bluetoothAdapter.isEnabled()) {
            Intent bluetoothEnableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            resultLauncher.launch(bluetoothEnableIntent);
        }
    }

    public void turnOffBluetooth() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_ADMIN
        ) == PackageManager.PERMISSION_GRANTED) {
            bluetoothAdapter.disable();
        }
        isBluetoothOn.postValue(false);
    }

    public void scanNearbyDevice() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED) {
            bluetoothAdapter.startDiscovery();
        }
    }

    public void stopScanningNearbyDevice() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED) {
            bluetoothAdapter.cancelDiscovery();
        }
    }

    public void discoverNewDevice(String deviceRealName, String deviceAddress) {
        if (!deviceRealName.startsWith("Damskuy_")) return;
        if (scannedDevicesMap.containsKey(deviceAddress)) return;

        String deviceName = deviceRealName.substring(8);
        scannedDevicesMap.put(deviceAddress, deviceRealName);

        ScannedDevice newDiscoveredDevice = new ScannedDevice(
                deviceRealName,
                deviceName,
                deviceAddress
        );

        ArrayList<ScannedDevice> currentData = scannedDevices.getValue();
        if (currentData == null) currentData = new ArrayList<>();

        currentData.add(newDiscoveredDevice);
        scannedDevices.setValue(currentData);
    }

    public void clearScannedDeviceList() {
        if (scannedDevices.getValue() != null) scannedDevices.getValue().clear();
        scannedDevicesMap.clear();
    }
}
