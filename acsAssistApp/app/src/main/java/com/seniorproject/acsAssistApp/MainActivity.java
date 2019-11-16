package com.seniorproject.acsAssistApp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        boolean darkToggle = sharedPreferences.getBoolean("dark_toggle", false);                // Utilize the toggle from the settings page to handle themeing
        if (darkToggle) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);                   // Enable android night mode when toggle set
        }

        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        setContentView(R.layout.activity_main);                                                        // The content view is based on activity_main.xml
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer);                                                           // Finding the drawer (defined in activity_main.xml)
        NavigationView navigationView = findViewById(R.id.nav_view);                                  // Finding the navigation view (defined in activity_main.xml)
        navigationView.setNavigationItemSelectedListener(this);                                       // Setting a listener for the navigation view
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);         // Setting the toggle & adding a listener
        drawer.addDrawerListener(toggle);
        toggle.syncState();                                                                            // Sync the hamburger icon with the drawer status

        if (savedInstanceState == null) {                                                              // Upon initial setup, home page should be shown
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Home()).commit();
            navigationView.setCheckedItem(R.id.home);
            setTitle("Home");
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {                                                                    // Create a new fragment for each menu item selected
            case R.id.home: {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Home()).commit();
                setTitle("Home");
                break;
            }

            case R.id.beginExercise: {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BeginExercise()).commit();
                setTitle("Begin Exercise");
                break;
            }

            case R.id.viewHistory: {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ViewHistory()).commit();
                setTitle("View History");
                break;
            }

            case R.id.chat: {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Chat()).commit();
                setTitle("Chat");
                break;
            }

            case R.id.settings: {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Settings()).commit();
                setTitle("Settings");
                break;
            }

            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Home()).commit();
                setTitle("Home");
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {                                                               // Close the drawer if the back icon is pressed
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}