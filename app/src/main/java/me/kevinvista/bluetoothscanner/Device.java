package me.kevinvista.bluetoothscanner;

public class Device{

    private String name;

    private String address;

    private boolean paired;

    private short signal;

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public boolean isPaired() {
        return paired;
    }

    public short getSignal() {
        return signal;
    }

    public Device(String name, boolean paired, String address, short signal) {
        this.name = name;
        this.paired = paired;
        this.address = address;
        this.signal = signal;
    }
}