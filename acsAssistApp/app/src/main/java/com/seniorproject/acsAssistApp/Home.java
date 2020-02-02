package com.seniorproject.acsAssistApp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class Home extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Toast toast = Toast.makeText(getActivity(),
                "Home page coming soon!",
                Toast.LENGTH_SHORT);
        toast.show();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

}