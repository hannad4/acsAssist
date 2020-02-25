package com.seniorproject.acsAssistApp;

//acsAssist MAC:        E5:A5:53:32:BD:7C
//angle service:        00001826-0000-1000-8000-00805f9b34fb
//roll characteristic:  78c5307b-6715-4040-bd50-d64db33e2e9e
//pitch characteristic: 78c5307a-6715-4040-bd50-d64db33e2e9e

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class BeginExercise extends Fragment {
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothDevice arduino = bluetoothAdapter.getRemoteDevice("E5:A5:53:32:BD:7C");
    Boolean showMeasurements = false;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View currentView = inflater.inflate(R.layout.fragment_begin_exercise, container, false);
        final TextView instructionText = currentView.findViewById(R.id.instructionText);
        final RadioGroup exerciseButtons = currentView.findViewById(R.id.exerciseButtons);
        final Button emergencyButton = currentView.findViewById(R.id.emergencyButton);
        Button beginExercise = currentView.findViewById(R.id.beginExercise);
        emergencyButton.setVisibility(View.INVISIBLE);

        if (bluetoothAdapter == null) {                     // Bluetooth not supported
            Toast.makeText(getActivity(), "This device does not support Bluetooth! Use a Bluetooth enabled device.", Toast.LENGTH_LONG).show();
        }

        if (!bluetoothAdapter.isEnabled()) {                // If adapter is not enabled, request to enable it from within the app
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }

        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
            permissionCheck += ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);
            if (permissionCheck != 0) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }

        arduino.connectGatt(this.getActivity(), true, gattCallback);

        beginExercise.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int selectedExerciseID = exerciseButtons.getCheckedRadioButtonId();
                RadioButton selectedExercise = currentView.findViewById(selectedExerciseID);
                Log.i("BUTTON", String.valueOf(selectedExercise));

                currentView.findViewById(R.id.exerciseButtons).setVisibility(View.INVISIBLE);
                currentView.findViewById(R.id.beginExercise).setVisibility(View.INVISIBLE);
                currentView.findViewById(R.id.emergencyButton).setVisibility(View.VISIBLE);
                currentView.findViewById(R.id.emergencyButton).setBackgroundColor(0xFFFF0000);


                final CountDownTimer cTimer = new CountDownTimer(3000, 1000) {
                    @Override
                    public void onTick(long l) {
                        instructionText.setText("Now Heating \n" +new SimpleDateFormat("mm:ss").format(new Date( l)));
                    }

                    @Override
                    public void onFinish() {
                        currentView.findViewById(R.id.emergencyButton).setVisibility(View.INVISIBLE);
                        showMeasurements = true;
                    }
                };
                cTimer.start();

                currentView.findViewById(R.id.emergencyButton).setOnClickListener(new View.OnClickListener() {
                    public void onClick (View v) {
                        instructionText.setText("Exercise Canceled");
                        currentView.findViewById(R.id.emergencyButton).setVisibility(View.INVISIBLE);
                        cTimer.cancel();
                    }
                });
            }
        });

        return currentView;
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        List<BluetoothGattCharacteristic> chars = new ArrayList<>();

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            final TextView instructionText = getView().findViewById(R.id.instructionText);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    instructionText.setText("Connected. Select Exercise");
                    gatt.discoverServices();
                    break;

                case BluetoothProfile.STATE_DISCONNECTED:
                    instructionText.setText("acsAssist Disconnected");
                    break;

                case BluetoothProfile.STATE_CONNECTING:
                    instructionText.setText("Connecting to acsAssist...");
                    break;

                default:
                    instructionText.setText("acsAssist Disconnected");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                for (BluetoothGattCharacteristic characteristic: gatt.getService(UUID.fromString("00001826-0000-1000-8000-00805f9b34fb")).getCharacteristics()) {
                    gatt.setCharacteristicNotification(characteristic, true);
                    BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    gatt.writeDescriptor(descriptor);
                    chars.add(characteristic);
                }

            }
            //requestCharacteristics(gatt);
        }
        public void requestCharacteristics(BluetoothGatt gatt) {
            gatt.readCharacteristic(chars.get(chars.size()-1));
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            TextView instructionText = getView().findViewById(R.id.instructionText);
            if(characteristic.getUuid().toString().equals("78c5307a-6715-4040-bd50-d64db33e2e9e")) {
                instructionText.setText("lol");
                Log.i("WOW", "Read Characteristic 1");
            }
            else if(characteristic.getUuid().toString().equals("78c5307b-6715-4040-bd50-d64db33e2e9e")) {
                instructionText.setText("ok");
                Log.i("WOW", "Read Characteristic 2");
            }
            chars.remove(chars.get(chars.size()-1));

            if(chars.size() > 0) {
                Log.i("WOW", "requested again");
                requestCharacteristics(gatt);
            }


            if(showMeasurements) {
//                instructionText.setText(characteristic.getStringValue(0));
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            TextView instructionText = getView().findViewById(R.id.instructionText);
            if(showMeasurements) {
                instructionText.setText(characteristic.getStringValue(0));
            }

        }
    };
}