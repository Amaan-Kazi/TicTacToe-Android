package com.amaan.tictactoe;

import android.bluetooth.BluetoothSocket;

public class BluetoothManager {

    private static BluetoothSocket socket;

    public static void setSocket(BluetoothSocket s) {
        socket = s;
    }

    public static BluetoothSocket getSocket() {
        return socket;
    }
}