package com.damskuy.petfeedermobileapp.ui.devices;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.damskuy.petfeedermobileapp.R;
import com.damskuy.petfeedermobileapp.data.model.ScannedDevice;

import java.util.ArrayList;

public class ScannedDevicesListAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<ScannedDevice> scannedDevices;

    public ScannedDevicesListAdapter(Context context, ArrayList<ScannedDevice> scannedDevices) {
        this.context = context;
        this.scannedDevices = scannedDevices;
    }

    @Override
    public int getCount() {
        return scannedDevices.size();
    }

    @Override
    public Object getItem(int position) {
        return scannedDevices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ScannedDeviceHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.view_holder_scanned_device, parent, false);
            holder = new ScannedDeviceHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ScannedDeviceHolder) convertView.getTag();
        }

        ScannedDevice scannedDevice = scannedDevices.get(position);
        holder.deviceName.setText(scannedDevice.getDeviceName());

        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddNewDeviceActivity.class);
            intent.putExtra("device-data", scannedDevice);
            context.startActivity(intent);
        });

        return convertView;
    }

    private static class ScannedDeviceHolder {

        private final TextView deviceName;

        public ScannedDeviceHolder(View view) {
            deviceName = view.findViewById(R.id.txt_device_name_scanned_device_holder);
        }
    }
}
