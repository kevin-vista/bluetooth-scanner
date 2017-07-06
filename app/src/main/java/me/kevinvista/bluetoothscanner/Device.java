package me.kevinvista.bluetoothscanner;

public class Device{

    private String name;

    private String address;

    private boolean paired;

    private short signal;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isPaired() {
        return paired;
    }

    public void setPaired(boolean paired) {
        this.paired = paired;
    }

    public short getSignal() {
        return signal;
    }

    public void setSignal(short signal) {
        this.signal = signal;
    }

    public Device(String name, boolean paired, String address, short signal) {
        this.name = name;
        this.paired = paired;
        this.address = address;
        this.signal = signal;
    }
}