package com.damskuy.petfeedermobileapp.data.model;

import java.io.Serializable;

public class ScannedDevice implements Serializable {

    private final String deviceRealName;
    private final String deviceName;
    private final String deviceAddress;

    public ScannedDevice(String deviceRealName, String deviceName, String deviceAddress) {
        this.deviceRealName = deviceRealName;
        this.deviceName = deviceName;
        this.deviceAddress = deviceAddress;
    }

    public String getRealName() {
        return deviceRealName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }
}
