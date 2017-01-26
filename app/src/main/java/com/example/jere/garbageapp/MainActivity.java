package com.example.jere.garbageapp;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
        Menu menu;
        DrawerLayout drawer;
        Toolbar toolbar;
        private SessionManager sessionManager;
        ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    android.support.v4.app.FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.transparent);
        toolbar.setTitle("HOME");
        sessionManager =new SessionManager(getApplicationContext());
        getNavigation();
        initFragment();
    }
    private  void getNavigation(){
        drawer= (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void initFragment(){
        if(sessionManager.loggedIn()){
            manageNavView();
            createFragment(new EventsFragment(),"EVENTS");
        }
        else {
            manageNavView();
            createFragment(new LoginFragment(),"LOGIN");
        }
    }



    private void manageNavView(){
        DrawerLayout drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menuNav = navigationView.getMenu();
        MenuItem loginItem = menuNav.findItem(R.id.nav_login);
        MenuItem registerItem = menuNav.findItem(R.id.nav_register);
        MenuItem changeItem = menuNav.findItem(R.id.nav_profile);
        MenuItem name=menuNav.findItem(R.id.nav_header_main_name);
        MenuItem email=menuNav.findItem(R.id.nav_header_main_email);

        if(sessionManager.loggedIn()){
            loginItem.setVisible(false);
            registerItem.setVisible(false);
            changeItem.setVisible(true);
            getNavigation();
            //name.setTitle(sessionManager.getname());
            //email.setTitle(sessionManager.getemail());
            //navigationView.inflateMenu(R.menu.activity_main_drawer);
        }else{
            changeItem.setVisible(false);
            loginItem.setVisible(true);
            registerItem.setVisible(true);
            getNavigation();
           // name.setTitle("");
            //email.setTitle("");
//            navigationView.inflateMenu(R.menu.activity_main_drawer);

        }

    }
    private void logout(){
        sessionManager.setLoggedIn(false);
        finish();
    }

    private void loginFragment(){
        createFragment(new LoginFragment(),"LOGIN");
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
        MenuItem item = menu.findItem(R.id.logout);
        if(sessionManager.loggedIn()){
            item.setVisible(true);
        }

        invalidateOptionsMenu();
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
            createFragment(new EventsFragment(),"EVENTS");
        }
        if(id==R.id.logout){
            logout();
        }

        return super.onOptionsItemSelected(item);
    }
    private  void createFragment(Fragment fragment,String fragmentName){
        fragmentTransaction =getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_activity_container,fragment);
        fragmentTransaction.commit();
        getSupportActionBar().setTitle(fragmentName);
    }

    private void showToast(){
        Snackbar snackbar= Snackbar.make(findViewById(R.id.content_main),"You are not logged In.Login to Continue",Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        manageNavView();
        if (id == R.id.nav_home) {
            createFragment(new HomeFragment(),"HOME");
            //getSupportActionBar().setLogo(R.drawable.transparent);
            item.setChecked(true);
        } else if (id == R.id.nav_events) {
            if(!sessionManager.loggedIn()){
                showToast();
            }else {
                createFragment(new EventsFragment(),"EVENTS");
                item.setChecked(true);
            }
        }
        else if (id == R.id.nav_complain) {
            if(!sessionManager.loggedIn()){
                showToast();
                loginFragment();
            }else {
                createFragment(new ComplainFragment(),"REPORT");
                item.setChecked(true);
            }
        }
        else if (id == R.id.nav_myevents) {
            if(!sessionManager.loggedIn()){
                showToast();
                loginFragment();
            }else {
                createFragment(new MyEventsFragment(),"MY EVENTS");
                item.setChecked(true);
            }
        }
        else if (id == R.id.nav_manage_response) {
            if(!sessionManager.loggedIn()){
                showToast();
                loginFragment();
            }else {
                createFragment(new FragmentComplainResponse(),"RESPONSES");
                item.setChecked(true);
            }

        }
        else if (id == R.id.nav_register) {
            createFragment(new RegisterFragment(),"REGISTER WITH US");
            item.setChecked(true);

        } else if (id == R.id.nav_login) {
            createFragment(new LoginFragment(),"LOGIN");
            item.setChecked(true);
        } else if (id == R.id.nav_profile) {
            if(!sessionManager.loggedIn()){
                showToast();
                loginFragment();
            }else {
                createFragment(new ProfileFragment(),"CHANGE PROFILE");
                item.setChecked(true);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
