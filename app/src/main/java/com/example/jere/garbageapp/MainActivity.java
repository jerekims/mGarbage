package com.example.jere.garbageapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jere.garbageapp.Fragments.ComplainFragment;
import com.example.jere.garbageapp.Fragments.FragmentComplainResponse;
import com.example.jere.garbageapp.Fragments.HomeFragment;
import com.example.jere.garbageapp.Fragments.LoginFragment;
import com.example.jere.garbageapp.Fragments.MyEventsFragment;
import com.example.jere.garbageapp.Fragments.ProfileFragment;
import com.example.jere.garbageapp.Fragments.RegisterFragment;
import com.example.jere.garbageapp.libraries.Constants;

import static com.example.jere.garbageapp.R.id.drawer_layout;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
        private SharedPreferences pref;
        DrawerLayout drawer;
        Toolbar toolbar;
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        pref = getPreferences(0);
        initFragment();

    }
    private void initFragment(){
        Fragment fragment;
        if(pref.getBoolean(Constants.IS_LOGGED_IN,false)){
            fragment = new ProfileFragment();
        }else {
            fragment = new LoginFragment();
        }
        fragmentTransaction =getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_activity_container,fragment);
        fragmentTransaction.commit();
        getSupportActionBar().setTitle("LOGIN");

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
            fragmentTransaction.replace(R.id.main_activity_container,new HomeFragment());
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("HOME");
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
        } else if (id == R.id.nav_complain) {
            fragmentTransaction =getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_activity_container,new ComplainFragment());
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("REPORT A COMPLAINT");
            item.setChecked(true);
        }
        else if (id == R.id.nav_events) {
            fragmentTransaction =getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_activity_container,new MyEventsFragment());
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("MY EVENTS");
            item.setChecked(true);
        }
        else if (id == R.id.nav_manage_response) {
            fragmentTransaction =getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_activity_container,new FragmentComplainResponse());
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("RESPONSES");
            item.setChecked(true);

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
            fragmentTransaction =getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_activity_container,new ProfileFragment());
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("CHANGE PROFILE");
            item.setChecked(true);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
