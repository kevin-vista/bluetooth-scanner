package me.kevinvista.bluetoothscanner;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    public static final String TAG = "MainActivity";

    public static Context contextOfApplication;

    private int ACCESS_COARSE_LOCATION_CODE = 1;

    private int REQUEST_ENABLE_BLUETOOTH = 2;

    private int SCAN_MODE_ERROR = 3;

    private boolean bluetoothReceiverRegistered;

    private boolean scanModeReceiverRegistered;

    private SwipeRefreshLayout swipeRefreshLayout;

    TextView textDeviceName;

    TextView textDeviceMac;

    TextView textDevicePaired;

    TextView textDeviceSignal;

    private BluetoothAdapter mBluetoothAdapter;

    private BluetoothDevice mBluetoothDevice;

    private RecyclerView recyclerView;

    private DeviceAdapter deviceAdapter;

    private List<Device> devices = new ArrayList<>();

    private final Handler handler = new Handler();
    Runnable scanTask = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, 3000);
            scanBluetooth();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contextOfApplication = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        //Request Permission
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "获取权限", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_COARSE_LOCATION_CODE);
            }
        }

        initData();
        handler.post(scanTask);
        deviceAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(scanTask);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        handler.post(scanTask);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothReceiverRegistered) {
            unregisterReceiver(bluetoothReceiver);
        }
        if (scanModeReceiverRegistered) {
            unregisterReceiver(scanModeReceiver);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set_visible:
                Intent visibleIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                visibleIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
                startActivity(visibleIntent);
                Toast.makeText(this, "请返回应用以授权", Toast.LENGTH_LONG).show();
                //register scanModeReceiver
                scanModeReceiverRegistered = true;
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
                registerReceiver(scanModeReceiver, intentFilter);
            case R.id.settings:
                /*
                //Go to system settings
                Intent settingsIntent = new Intent();
                settingsIntent.setAction(Settings.ACTION_BLUETOOTH_SETTINGS);
                settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    startActivity(settingsIntent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed going to settings", Toast.LENGTH_SHORT).show();
                }
                */
                Intent intent = new Intent(this, SettingsFragment.class);
                startActivity(intent);
                break;
            case R.id.about:
                Toast.makeText(this, "Written by kevin-vista", Toast.LENGTH_SHORT).show();
                break;
            case R.id.feedback:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:kevin-vista@outlook.com"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback - Bluetooth Scanner");
                startActivity(emailIntent);
            default:
                break;
        }
        return true;
    }

    private void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        textDeviceName = (TextView) findViewById(R.id.text_name);
        textDeviceMac = (TextView) findViewById(R.id.text_address);
        textDeviceSignal = (TextView) findViewById(R.id.text_signal);
        textDevicePaired = (TextView) findViewById(R.id.text_paired);
        deviceAdapter = new DeviceAdapter(devices);
        recyclerView.setAdapter(deviceAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initData() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    private void scanBluetooth() {
        bluetoothReceiverRegistered = true;
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(bluetoothReceiver, filter);
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();
    }
    @Override
    public void onRefresh() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mBluetoothAdapter != null) {
                    if (!mBluetoothAdapter.isEnabled()) {
                        //mBluetoothAdapter.enable();
                        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
                    }
                    handler.post(scanTask);}
                deviceAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: Execute");
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                boolean  paired = device.getBondState() == BluetoothDevice.BOND_BONDED;
                String deviceAddress = device.getAddress();
                short deviceRSSI = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI, (short) 0);
                Device mDevice = new Device(deviceName, paired, deviceAddress, deviceRSSI);

                devices.remove(scannedDevice(mDevice));
                devices.add(mDevice);
                deviceAdapter.notifyDataSetChanged();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (devices.size() == 0) {
                    Log.d(TAG, "onReceive: No device");
                }
            }
        }

        private Device scannedDevice(Device d) {
            for (Device device : devices) {
                if (d.getAddress().equals(device.getAddress())) {
                    return device;
                }
            }
            return null;
        }

        /*private boolean containsDevice(Device d) {
            for (Device device : devices) {
                if (d.getAddress().equals(device.getAddress())) {
                    return true;
                }
            }
            return false;
        }*/
    };

    private BroadcastReceiver scanModeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int scanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, SCAN_MODE_ERROR);
            if (scanMode == BluetoothAdapter.SCAN_MODE_CONNECTABLE
                    || scanMode == BluetoothAdapter.SCAN_MODE_NONE) {
                Toast.makeText(context, "设备对外不可见", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }

}