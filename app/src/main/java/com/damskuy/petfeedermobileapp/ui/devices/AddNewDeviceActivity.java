package com.damskuy.petfeedermobileapp.ui.devices;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.damskuy.petfeedermobileapp.R;
import com.damskuy.petfeedermobileapp.data.model.ScannedDevice;
import com.damskuy.petfeedermobileapp.utils.ViewUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddNewDeviceActivity extends AppCompatActivity {

    private ScannedDevice deviceData;
    private TextInputEditText edtWifiSSID, edtWifiPassword;
    private TextInputLayout edtLayoutWifiSSID, edtLayoutWifiPassword;
    private AppCompatButton btnAdd, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_device);
        Intent intent = getIntent();
        deviceData = (ScannedDevice) intent.getSerializableExtra("device-data");
        initUI();
        initHandlers();
    }

    private void initUI() {
        TextView txtDeviceName = findViewById(R.id.txt_device_name_add_device);
        txtDeviceName.setText(deviceData.getDeviceName());
        edtWifiSSID = findViewById(R.id.edt_wifi_ssid);
        edtWifiPassword = findViewById(R.id.edt_wifi_password);
        edtLayoutWifiSSID = findViewById(R.id.edt_layout_wifi_ssid);
        edtLayoutWifiPassword = findViewById(R.id.edt_layout_wifi_password);
        btnAdd = findViewById(R.id.btn_add_device);
        btnCancel = findViewById(R.id.btn_cancel_add_device);
        textInputConfig();
    }

    private void textInputConfig() {
        String edtWifiSSIDHint = getString(R.string.enter_wi_fi_ssid);
        String edtWifiPasswordHint = getString(R.string.enter_wi_fi_password);

        edtWifiSSID.setOnFocusChangeListener((v, hasFocus) ->
                ViewUtils.hideTextInputHint(hasFocus, edtWifiSSID, edtLayoutWifiSSID, edtWifiSSIDHint));

        edtWifiPassword.setOnFocusChangeListener((v, hasFocus) ->
                ViewUtils.hideTextInputHint(hasFocus, edtWifiPassword, edtLayoutWifiPassword, edtWifiPasswordHint));
    }

    private void initHandlers() {
       btnCancel.setOnClickListener(v -> onBackPressed());
    }
}