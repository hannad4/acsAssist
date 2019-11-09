package com.seniorproject.acsAssistApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerLayout;                                                                 // The drawer
    private ActionBarDrawerToggle mToggle;                                                              // The toggle

    private void setNavigationViewListener() {                                                          // Need to set listeners for all menu items
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = findViewById(R.id.drawer);                                                       // Finding the drawer (defined in activity_main.xml)
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close); // Linking the menu bar toggle to activate the drawer
        mDrawerLayout.addDrawerListener(mToggle);                                                       // The drawer will listen for the toggle being enabled
        mToggle.syncState();                                                                            // Synchronize the icon animation/changing with the drawer being activated
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);                                         // Make back icon clickable
        setNavigationViewListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {                                                      // Return true if menu toggle is activated
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {                                   // Perform an action for each item in menu. Just putting toast messages for now
        switch (item.getItemId()) {

            case R.id.startExercise: {
                Toast toast = Toast.makeText(getApplicationContext(), "Start Exercise was selected", Toast.LENGTH_LONG);
                toast.show();
                break;
            }

            case R.id.viewHistory: {
                Toast toast = Toast.makeText(getApplicationContext(), "View History was selected", Toast.LENGTH_LONG);
                toast.show();
                break;
            }

            case R.id.settings: {
                Toast toast = Toast.makeText(getApplicationContext(), "Settings was selected", Toast.LENGTH_LONG);
                toast.show();
                break;
            }
        }
        //close navigation drawer
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
