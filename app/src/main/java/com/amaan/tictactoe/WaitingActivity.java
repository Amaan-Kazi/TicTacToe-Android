package com.amaan.tictactoe;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Build;
import android.os.Bundle;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class WaitingActivity extends AppCompatActivity {

    private BluetoothAdapter adapter;

    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 1;

    private static final UUID GAME_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        adapter = BluetoothAdapter.getDefaultAdapter();

        if (adapter == null) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Request runtime permissions first
        if (!hasBluetoothPermissions()) {
            requestBluetoothPermissions();
            return;
        }

        // Now it is safe to call Bluetooth APIs
        try {

            if (!adapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivity(enableIntent);
            }

        } catch (SecurityException e) {
            Toast.makeText(this, "Bluetooth permission missing", Toast.LENGTH_LONG).show();
            requestBluetoothPermissions();
            return;
        }

        Button hostButton = findViewById(R.id.hostButton);
        Button joinButton = findViewById(R.id.joinButton);

        hostButton.setOnClickListener(v -> hostGame());
        joinButton.setOnClickListener(v -> joinGame());
    }
    /*
     PERMISSION CHECK
     */
    private boolean hasBluetoothPermissions() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            return true;
        }

        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED

                &&

                ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_SCAN
                ) == PackageManager.PERMISSION_GRANTED;
    }

    /*
     REQUEST PERMISSION
     */
    private void requestBluetoothPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.BLUETOOTH_CONNECT,
                            Manifest.permission.BLUETOOTH_SCAN
                    },
                    REQUEST_BLUETOOTH_PERMISSIONS
            );
        }
    }

    /*
     PERMISSION RESULT
     */
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {

            boolean granted = true;

            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    granted = false;
                    break;
                }
            }

            if (!granted) {
                Toast.makeText(
                        this,
                        "Bluetooth permissions are required",
                        Toast.LENGTH_LONG
                ).show();

                finish();
            }
        }
    }

    /*
     HOST GAME
     */
    private void hostGame() {

        Toast.makeText(this, "Waiting for player...", Toast.LENGTH_SHORT).show();

        new Thread(() -> {

            try {

                if (!hasBluetoothPermissions()) return;

                BluetoothServerSocket server =
                        adapter.listenUsingRfcommWithServiceRecord(
                                "TicTacToe",
                                GAME_UUID
                        );

                BluetoothSocket socket = server.accept();

                BluetoothManager.setSocket(socket);

                runOnUiThread(() -> {
                    startActivity(new Intent(this, GameActivity.class));
                    finish();
                });

            } catch (IOException | SecurityException e) {
                e.printStackTrace();
            }

        }).start();
    }

    /*
     JOIN GAME
     */
    private void joinGame() {

        if (!hasBluetoothPermissions()) {
            requestBluetoothPermissions();
            return;
        }

        try {

            Set<BluetoothDevice> paired = adapter.getBondedDevices();

            if (paired.isEmpty()) {
                Toast.makeText(this, "No paired devices", Toast.LENGTH_SHORT).show();
                return;
            }

            ArrayList<BluetoothDevice> deviceList = new ArrayList<>(paired);
            ArrayList<String> names = new ArrayList<>();

            for (BluetoothDevice d : deviceList) {
                names.add(d.getName() + "\n" + d.getAddress());
            }

            ListView listView = new ListView(this);

            listView.setAdapter(
                    new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_list_item_1,
                            names
                    )
            );

            setContentView(listView);

            listView.setOnItemClickListener((parent, view, position, id) -> {

                BluetoothDevice device = deviceList.get(position);

                connectToDevice(device);
            });

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /*
     CONNECT TO HOST
     */
    private void connectToDevice(BluetoothDevice device) {

        Toast.makeText(this, "Connecting...", Toast.LENGTH_SHORT).show();

        new Thread(() -> {

            try {

                if (!hasBluetoothPermissions()) return;

                BluetoothSocket socket =
                        device.createRfcommSocketToServiceRecord(GAME_UUID);

                adapter.cancelDiscovery();

                socket.connect();

                BluetoothManager.setSocket(socket);

                runOnUiThread(() -> {
                    startActivity(new Intent(this, GameActivity.class));
                    finish();
                });

            } catch (IOException | SecurityException e) {
                e.printStackTrace();
            }

        }).start();
    }
}