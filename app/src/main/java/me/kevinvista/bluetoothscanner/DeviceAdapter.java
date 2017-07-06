package me.kevinvista.bluetoothscanner;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceHolder> {

    public List<Device> mDeviceList;

    static class DeviceHolder extends RecyclerView.ViewHolder {

        TextView textDeviceName;
        TextView textDeviceAddress;
        TextView textDeviceSignal;
        TextView textDevicePaired;

        public DeviceHolder(View itemView) {
            super(itemView);
            textDeviceName = (TextView) itemView.findViewById(R.id.text_name);
            textDeviceAddress = (TextView) itemView.findViewById(R.id.text_address);
            textDeviceSignal = (TextView) itemView.findViewById(R.id.text_signal);
            textDevicePaired = (TextView) itemView.findViewById(R.id.text_paired);
        }
    }

    public DeviceAdapter(List<Device> deviceList) {
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
    public void onBindViewHolder(DeviceHolder holder, int position) {
        Device device = mDeviceList.get(position);
        holder.textDeviceName.setText(device.getName());
        holder.textDeviceAddress.setText("MAC: " + device.getAddress());
        holder.textDeviceSignal.setText(device.getSignal() + "dB");
        //holder.textDevicePaired.setText(device.isPaired() + "");
    }

    @Override
    public int getItemCount() {
        return mDeviceList.size();
    }
}