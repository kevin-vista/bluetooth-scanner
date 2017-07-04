package me.kevinvista.bluetoothscanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
    private List<BluetoothDevice> mDeviceList =

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDevices();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        TextView textDeviceName = (TextView) findViewById(R.id.text_name);
        TextView textDeviceMac = (TextView) findViewById(R.id.text_mac);
        TextView textDeviceSignal = (TextView) findViewById(R.id.text_signal);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DeviceAdapter deviceAdapter = new DeviceAdapter();
        recyclerView.setAdapter(deviceAdapter);

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {

            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:

                break;
            case R.id.sys_settings:

                break;
            case R.id.intro:

                break;
            case R.id.about:

                break;
            default:
                break;
        }
        return true;
    }

    private void initDevices() {

    }
}
