package org.team1515.morscout.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.team1515.morscout.R;
import org.team1515.morscout.fragment.main.FeedbackFragment;
import org.team1515.morscout.fragment.main.HelpFragment;
import org.team1515.morscout.fragment.main.HomeFragment;
import org.team1515.morscout.fragment.main.MatchesFragment;
import org.team1515.morscout.fragment.main.SettingsFragment;
import org.team1515.morscout.fragment.main.TeamListFragment;
import org.team1515.morscout.network.CookieRequest;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences preferences;
    private RequestQueue queue;

    // Fragments
    private Fragment homeFrag,
            matchesFrag,
            teamListFrag,
            settingsFrag,
            helpFrag,
            feedbackFrag;

    // Navigation Drawer
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(null, 0);
        queue = Volley.newRequestQueue(this);

        //Fragments
        homeFrag = new HomeFragment();
        matchesFrag = new MatchesFragment();
        teamListFrag = new TeamListFragment();
        settingsFrag = new SettingsFragment();
        helpFrag = new HelpFragment();
        feedbackFrag = new FeedbackFragment();

        //Set default fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame, homeFrag).commit();

        // Set up action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Exo2-Medium.ttf");
        toolbarTitle.setTypeface(typeface);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        // Set up navigation drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);

        // Listen for navigation events
        NavigationView navigationView = (NavigationView) findViewById(R.id.main_nav);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // Checking for the "menu" key
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            // Toggle drawer state
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
            // Checking for the "back" key
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }

    @Override
    public void onBackPressed() {
        // Close drawer with back button
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else { // If drawer not open, use default usage
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        item.setChecked(true);

        drawerLayout.closeDrawer(GravityCompat.START);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()) {
            case R.id.nav_item_1:
                transaction.replace(R.id.main_frame, homeFrag);
                break;
            case R.id.nav_item_2:
                transaction.replace(R.id.main_frame, matchesFrag);
                break;
            case R.id.nav_item_3:
                transaction.replace(R.id.main_frame, teamListFrag);
                break;
            case R.id.nav_item_4:
                transaction.replace(R.id.main_frame, settingsFrag);
                break;
            case R.id.nav_item_5:
                transaction.replace(R.id.main_frame, helpFrag);
                break;
            case R.id.nav_item_6:
                transaction.replace(R.id.main_frame, feedbackFrag);
                break;
            case R.id.nav_item_7:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CookieRequest logoutRequest = new CookieRequest(Request.Method.POST,
                                "/logout",
                                preferences,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        preferences.edit().clear().apply();
                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        AlertDialog.Builder offlineLogout = new AlertDialog.Builder(MainActivity.this);
                                        offlineLogout.setMessage("Are you sure you want to log off while offline? You may not be able to log back in.");
                                        offlineLogout.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                preferences.edit().clear().apply();
                                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                        offlineLogout.setNegativeButton("Cancel", null);
                                        offlineLogout.create().show();
                                    }
                                });
                        queue.add(logoutRequest);
                    }
                });

                builder.setNegativeButton("Cancel", null);
                builder.create().show();
                break;
            default:
                transaction.replace(R.id.main_frame, homeFrag);
        }
        transaction.addToBackStack(null).commit();

        return true;
    }
}
