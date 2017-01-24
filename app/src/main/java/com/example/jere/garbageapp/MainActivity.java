package com.example.jere.garbageapp;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.jere.garbageapp.Fragments.ComplainFragment;
import com.example.jere.garbageapp.Fragments.EventsFragment;
import com.example.jere.garbageapp.Fragments.FragmentComplainResponse;
import com.example.jere.garbageapp.Fragments.HomeFragment;
import com.example.jere.garbageapp.Fragments.LoginFragment;
import com.example.jere.garbageapp.Fragments.MyEventsFragment;
import com.example.jere.garbageapp.Fragments.ProfileFragment;
import com.example.jere.garbageapp.Fragments.RegisterFragment;
import com.example.jere.garbageapp.libraries.SessionManager;

import static com.example.jere.garbageapp.R.id.drawer_layout;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
        private final String TAG="MAIN_ACTIVITY";
        private SharedPreferences pref;
        DrawerLayout drawer;
        Toolbar toolbar;
        private SessionManager sessionManager;
    android.support.v4.app.FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.transparent);
        toolbar.setTitle("HOME");

        drawer= (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        sessionManager =new SessionManager(getApplicationContext());
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initFragment();

    }
    private void initFragment(){
        Fragment fragment;
        if(sessionManager.loggedIn()){
            fragment = new EventsFragment();
            fragmentTransaction =getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_activity_container,fragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("EVENTS");
        }
        else {
            fragment = new LoginFragment();
            fragmentTransaction =getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_activity_container,fragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("LOGIN");
        }


    }
    private void logout(){
        sessionManager.setLoggedIn(false);
        finish();
        loginFragment();
    }
    private void loginFragment(){
        Fragment fragment;
        fragment = new LoginFragment();
        fragmentTransaction =getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_activity_container,fragment);
        fragmentTransaction.commit();
        getSupportActionBar().setTitle("LOGIN");
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        DrawerLayout drawer = (DrawerLayout) findViewById(drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (manager.getBackStackEntryCount() > 1 ) {
                // If there are back-stack entries, leave the FragmentActivity
                // implementation take care of them.
                manager.popBackStack();

            } else {
                // Otherwise, ask user if he wants to leave :)
                new AlertDialog.Builder(this)
                        .setTitle("Exit application.")
                        .setIcon(R.drawable.cancel)
                        .setMessage("Are you sure you want to exit?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                MainActivity.super.onBackPressed();
                            }
                        }).create().show();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id==R.id.action_refresh){
            fragmentTransaction =getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_activity_container,new EventsFragment());
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("EVENTS");
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragmentTransaction =getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_activity_container,new HomeFragment());
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("HOME");
            //getSupportActionBar().setLogo(R.drawable.transparent);
            item.setChecked(true);
        } else if (id == R.id.nav_events) {
            if(!sessionManager.loggedIn()){
                Toast.makeText(getApplicationContext(),"You are not logged In.",Toast.LENGTH_LONG).show();
            }else {
                //Toast.makeText(getApplicationContext(),sessionManager.getnumber(),Toast.LENGTH_LONG).show();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_activity_container, new EventsFragment());
                fragmentTransaction.commit();
                getSupportActionBar().setTitle("EVENTS");
                //getSupportActionBar().setLogo(R.drawable.transparent);
                item.setChecked(true);
            }
        }
        else if (id == R.id.nav_complain) {
            if(!sessionManager.loggedIn()){
                Toast.makeText(getApplicationContext(),"You are not logged In.Login to Continue",Toast.LENGTH_LONG).show();
                loginFragment();
            }else {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_activity_container, new ComplainFragment());
                fragmentTransaction.commit();
                getSupportActionBar().setTitle("REPORT");
                item.setChecked(true);
            }
        }
        else if (id == R.id.nav_myevents) {
            if(!sessionManager.loggedIn()){
                Toast.makeText(getApplicationContext(),"You are not logged In.",Toast.LENGTH_LONG).show();
                loginFragment();
            }else {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_activity_container, new MyEventsFragment());
                fragmentTransaction.commit();
                getSupportActionBar().setTitle("MY EVENTS");
                item.setChecked(true);
            }
        }
        else if (id == R.id.nav_manage_response) {
            if(!sessionManager.loggedIn()){
                Toast.makeText(getApplicationContext(),"You are not logged In.",Toast.LENGTH_LONG).show();
                loginFragment();
            }else {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_activity_container, new FragmentComplainResponse());
                fragmentTransaction.commit();
                getSupportActionBar().setTitle("RESPONSES");
                item.setChecked(true);
            }

        }
        else if (id == R.id.nav_register) {
            fragmentTransaction =getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_activity_container,new RegisterFragment());
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("REGISTER WITH US");
            item.setChecked(true);

        } else if (id == R.id.nav_login) {
            fragmentTransaction =getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_activity_container,new LoginFragment());
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("LOGIN");
            item.setChecked(true);
        } else if (id == R.id.nav_profile) {
            if(!sessionManager.loggedIn()){
                Toast.makeText(getApplicationContext(),"You are not logged In.",Toast.LENGTH_LONG).show();
                loginFragment();
            }else {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_activity_container, new ProfileFragment());
                fragmentTransaction.commit();
                getSupportActionBar().setTitle("CHANGE PROFILE");
                item.setChecked(true);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
