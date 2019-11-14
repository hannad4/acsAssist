package com.seniorproject.acsAssistApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);                                                         // The content view is based on activity_main.xml
        NavigationView navigationDrawer = findViewById(R.id.nav_view);                                  // Finding the navigation view (defined in activity_main.xml)
        mDrawerLayout = findViewById(R.id.drawer);                                                       // Finding the drawer (defined in activity_main.xml)
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close); // Linking the menu bar toggle to activate the drawer
        mDrawerLayout.addDrawerListener(mToggle);                                                       // The drawer will listen for the toggle being enabled
        mToggle.syncState();                                                                            // Synchronize the icon animation/changing with the drawer being activated
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);                  // Make back icon clickable

        FragmentTransaction init = getSupportFragmentManager().beginTransaction();                      // The init fragment transaction will be used for setting initial home content
        Class homeClass = Home.class;
        try {
            Fragment homeFragment = (Fragment) homeClass.newInstance();                                 // Create a new fragment from home class instance
            init.replace(R.id.flcontent, homeFragment).commit();                                        // Replace the current fragment content with home fragment's
            setTitle("Home");                                                                           // Set the title to home
        }
        catch (Exception e) {                                                                           // Need to place fragment switch in try catch b/c exception may occur
            e.printStackTrace();
        }
        setupDrawerContent(navigationDrawer);                                                           // Set listeners for the navigation drawer
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {                                                      // Return true if menu toggle is activated
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void selectItemDrawer(MenuItem item) {                                                       // Create the appropriate fragment given item selection
        Fragment currentFragment = null;
        Class fragmentClass;
        switch (item.getItemId()) {                                                                     // The fragment to create depends on id of item selected

            case R.id.home: {
                fragmentClass = Home.class;
                break;
            }

            case R.id.beginExercise: {
                fragmentClass = BeginExercise.class;
                break;
            }

            case R.id.viewHistory: {
                fragmentClass = ViewHistory.class;
                break;
            }

            case R.id.chat: {
                fragmentClass = Chat.class;
                break;
            }

            case R.id.settings: {
                fragmentClass = Settings.class;
                break;
            }

            default:
                fragmentClass = Home.class;
        }
        try {
            currentFragment = (Fragment) fragmentClass.newInstance();                                // Create the new instance of the fragment
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flcontent, currentFragment).commit();       // Replace the currently existing fragment w/ new one
        item.setChecked(true);
        setTitle(item.getTitle());                                                                  // The fragment title will depend on the title of the item

        mDrawerLayout.closeDrawer(GravityCompat.START);                                             // Close navigation drawer
    }
    private void setupDrawerContent(NavigationView navigationView) {                                // Set the listener for the drawer
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                selectItemDrawer(menuItem);                                                         // Run the function that creates the appropriate fragment
                return true;
            }
        });
    }
}