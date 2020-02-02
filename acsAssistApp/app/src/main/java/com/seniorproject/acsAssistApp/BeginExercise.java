package com.seniorproject.acsAssistApp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

// This is technically all we need initially to perform actions/viewable content per page
public class BeginExercise extends Fragment {
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View currentView = inflater.inflate(R.layout.fragment_begin_exercise, container, false);

        Button exercise1Button = currentView.findViewById(R.id.exercise1Button);
        Button exercise2Button = currentView.findViewById(R.id.exercise2Button);
        final Button emergencyButton = currentView.findViewById(R.id.exercise3Button);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {                     // Bluetooth not supported
            Toast.makeText(getActivity(), "This device does not support Bluetooth! Use a Bluetooth enabled device.", Toast.LENGTH_LONG).show();
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }

        exercise1Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//              getFragmentManager().beginTransaction().replace(R.id.fragment_container, new Home()).commit();
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
}