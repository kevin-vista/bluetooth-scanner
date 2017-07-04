package me.kevinvista.bluetoothscanner;

import android.bluetooth.BluetoothDevice;

public class Device extends BluetoothDevice{

    private String name;
    private String mac;
    private String signal;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getSignal() {
        return signal;
    }

    public void setSignal(String signal) {
        this.signal = signal;
    }

    public Device(String name, String mac, String signal) {
        this.name = name;
        this.mac = mac;
        this.signal = signal;
    }

}
