package com.seniorproject.acsAssistApp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

public class Settings extends PreferenceFragmentCompat {
    public SwitchPreference darkToggle;
    public Preference webLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        darkToggle = findPreference("dark_toggle");
        darkToggle.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {          // Set a listener for when toggle changes
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                if (darkToggle.isChecked()) {                                                           // Let the user know that changes have been registered
                    Toast.makeText(getActivity(),"Light Theme will be applied on app restart",Toast.LENGTH_LONG).show();
                    darkToggle.setChecked(false);
                }
                else {
                    Toast.makeText(getActivity(),"Dark Theme will be applied on app restart",Toast.LENGTH_LONG).show();
                    darkToggle.setChecked(true);
                }
                return true;
            }
        });

        webLink = findPreference("website_link");
        webLink.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
           @Override
           public boolean onPreferenceClick(Preference preference) {
               Intent intent = new Intent(Intent.ACTION_VIEW);
               intent.setData(Uri.parse("https://engprojects.tcnj.edu/acs-assist-20/"));                // navigate to senior project page through the intent
               startActivity(intent);
               return true;
           }
        });
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);                                         // the settings options are defined in preferences
    }
}
