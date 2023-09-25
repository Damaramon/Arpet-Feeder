package com.damskuy.petfeedermobileapp.ui.devices;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.damskuy.petfeedermobileapp.R;
import com.damskuy.petfeedermobileapp.data.model.Result;
import com.damskuy.petfeedermobileapp.utils.ViewUtils;

import java.util.ArrayList;

public class DevicesFragment extends Fragment {

    private View fragmentView;
    private SwitchCompat swTurnOnBluetooth;
    private ListView lvScannedDevices;
    private TextView txtScanningDevices;
    private DevicesViewModel devicesViewModel;
    private ActivityResultLauncher<Intent> bluetoothActivityResultLauncher;

    private final BroadcastReceiver discoverBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                txtScanningDevices.setVisibility(View.VISIBLE);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                txtScanningDevices.setVisibility(View.GONE);
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED) {
                    String deviceName = device.getName();
                    String deviceHardwareAddress = device.getAddress();
                    if (deviceName == null) deviceName = "";
                    if (deviceHardwareAddress == null) deviceHardwareAddress = "";
                    devicesViewModel.discoverNewDevice(deviceName, deviceHardwareAddress);
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_devices, container, false);
        bindViewModel();
        initUI();
        initResultLauncher();
        initHandlers();
        observeLiveData();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter discoverIntentFilter = new IntentFilter();
        discoverIntentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        discoverIntentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        discoverIntentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        requireActivity().registerReceiver(discoverBroadcastReceiver, discoverIntentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        devicesViewModel.stopScanningNearbyDevice();
        devicesViewModel.clearScannedDeviceList();
        requireActivity().unregisterReceiver(discoverBroadcastReceiver);
    }

    private void bindViewModel() {
        DevicesViewModelFactory factory = new DevicesViewModelFactory(
                requireActivity(),
                requireActivity().getSystemService(BluetoothManager.class)
        );
        devicesViewModel = new ViewModelProvider(this, factory).get(DevicesViewModel.class);
    }

    private void initUI() {
        swTurnOnBluetooth = fragmentView.findViewById(R.id.sw_bluetooth_devices);
        txtScanningDevices = fragmentView.findViewById(R.id.txt_scanning_devices);
        txtScanningDevices.setVisibility(View.GONE);
        lvScannedDevices = fragmentView.findViewById(R.id.lv_scanned_bluetooth_devices);
        devicesViewModel.checkBluetoothConnection();
    }

    private void initResultLauncher() {
        bluetoothActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    int resultCode = result.getResultCode();
                    if (resultCode == RESULT_OK) {
                        ViewUtils.fireSuccessAlert(getActivity(), "Bluetooth enabled");
                        devicesViewModel.checkBluetoothConnection();
                    }
                }
        );
    }

    private void initHandlers() {
        swTurnOnBluetooth.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                devicesViewModel.attemptConnectToBluetooth(bluetoothActivityResultLauncher);
            } else {
                devicesViewModel.turnOffBluetooth();
            }
        });
    }

    private void observeLiveData() {
       devicesViewModel.getIsBluetoothOn().observe(getViewLifecycleOwner(), isBluetoothOn -> {
           if (isBluetoothOn) {
               swTurnOnBluetooth.setChecked(true);
               devicesViewModel.scanNearbyDevice();
           } else {
               swTurnOnBluetooth.setChecked(false);
               devicesViewModel.stopScanningNearbyDevice();
           }
       });

       devicesViewModel.getBluetoothAvailability().observe(getViewLifecycleOwner(), bluetoothAvailability -> {
           if (bluetoothAvailability instanceof Result.Error) {
               String errorMessage = ((Result.Error<Boolean>) bluetoothAvailability).getErrorMessage();
               ViewUtils.fireErrorAlert(getActivity(), errorMessage);
           }
       });

       devicesViewModel.getScannedDevices().observe(getViewLifecycleOwner(), scannedDevices -> {
           if (scannedDevices == null) scannedDevices = new ArrayList<>();
           ScannedDevicesListAdapter adapter = new ScannedDevicesListAdapter(getActivity(), scannedDevices);
           lvScannedDevices.setAdapter(adapter);
       });
    }
}
