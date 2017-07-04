package me.kevinvista.bluetoothscanner;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceHolder> {

    public ArrayList<BluetoothDevice> mDeviceList;

    static class DeviceHolder extends RecyclerView.ViewHolder {

        TextView textDeviceName;
        TextView textDeviceAddress;
        TextView textDeviceSignal;

        public DeviceHolder(View itemView) {
            super(itemView);
            textDeviceName = (TextView) itemView.findViewById(R.id.text_name);
            textDeviceAddress = (TextView) itemView.findViewById(R.id.text_address);
            textDeviceSignal = (TextView) itemView.findViewById(R.id.text_signal);
        }
    }

    public DeviceAdapter(ArrayList<BluetoothDevice> deviceList) {
        mDeviceList = deviceList;
    }

    @Override
    public DeviceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_item, parent, false);
        DeviceHolder deviceHolder = new DeviceHolder(view);
        return deviceHolder;
    }

    @Override
    //TODO Get device signal strength and assign it to textDeviceSignal.
    public void onBindViewHolder(DeviceHolder holder, int position) {
        BluetoothDevice device = mDeviceList.get(position);
        holder.textDeviceName.setText(device.getName());
        holder.textDeviceAddress.setText(device.getAddress());
        holder.textDeviceSignal.setText();
    }

    @Override
    public int getItemCount() {
        return mDeviceList.size();
    }
}
