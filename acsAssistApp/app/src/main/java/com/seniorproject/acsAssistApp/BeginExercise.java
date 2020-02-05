package com.seniorproject.acsAssistApp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import static androidx.constraintlayout.widget.Constraints.TAG;

// This is technically all we need initially to perform actions/viewable content per page
public class BeginExercise extends Fragment {
//    private BluetoothDevice device;
//    private BluetoothSocket socket;
//    private OutputStream outputStream;
//    private InputStream inputStream;
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View currentView = inflater.inflate(R.layout.fragment_begin_exercise, container, false);
        Button exercise1Button = currentView.findViewById(R.id.exercise1Button);
        Button exercise2Button = currentView.findViewById(R.id.exercise2Button);
        final Button emergencyButton = currentView.findViewById(R.id.exercise3Button);


        if (bluetoothAdapter == null) {                     // Bluetooth not supported
            Toast.makeText(getActivity(), "This device does not support Bluetooth! Use a Bluetooth enabled device.", Toast.LENGTH_LONG).show();
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                if(deviceHardwareAddress == "ENTER ARDUINO MAC ADDRESS HERE") {
                    Thread initiatedConnecton = new ConnectThread(device);
                }
            }
        }

        exercise1Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                currentView.findViewById(R.id.exercise1Button).setVisibility(View.INVISIBLE);
                currentView.findViewById(R.id.exercise2Button).setVisibility(View.INVISIBLE);
                emergencyButton.setText("STOP HEATING");
                emergencyButton.setBackgroundColor(0xFFFF0000);
                final TextView timerText = currentView.findViewById(R.id.instructionText);
                final CountDownTimer cTimer = new CountDownTimer(10000, 1000) {
                    @Override
                    public void onTick(long l) {
                        timerText.setText("Now Heating \n" +new SimpleDateFormat("mm:ss").format(new Date( l)));
                    }

                    @Override
                    public void onFinish() {
                        timerText.setText("Heating Done!\nGet ready for exercise");


//                        emergencyButton.setVisibility(View.INVISIBLE);
                    }
                };
                cTimer.start();
                emergencyButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick (View v) {
                        timerText.setText("Heating Stopped");
                        emergencyButton.setVisibility(View.INVISIBLE);
                        cTimer.cancel();
                    }
                });
            }
        });

        return currentView;
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = device.createRfcommSocketToServiceRecord("INSERT ARDUINO SERVICE UUID HERE");
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
        //    manageMyConnectedSocket(mmSocket);              // At this point, should be able to get the input stream from the socket
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }
}