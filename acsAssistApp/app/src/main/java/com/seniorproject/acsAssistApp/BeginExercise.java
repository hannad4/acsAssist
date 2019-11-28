package com.seniorproject.acsAssistApp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.text.SimpleDateFormat;
import java.util.Date;


// This is technically all we need initially to perform actions/viewable content per page
public class BeginExercise extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View currentView = inflater.inflate(R.layout.fragment_begin_exercise, container, false);

        Button exercise1Button = currentView.findViewById(R.id.exercise1Button);
        Button exercise2Button = currentView.findViewById(R.id.exercise2Button);
        final Button emergencyButton = currentView.findViewById(R.id.exercise3Button);
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
                        emergencyButton.setVisibility(View.INVISIBLE);
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