package com.example.fourscreen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.fourscreen.fragments.scaling.ScalingFragment;
import com.example.fourscreen.fragments.list.IOnBackPressed;
import com.example.fourscreen.fragments.list.ListFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
                super.onBackPressed();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            Dialog alertDialog = builder.setTitle("Alert dialog!")
                    .setIcon(R.drawable.ic_list)
                    .setMessage("You want close?")
                    .setPositiveButton("OK", onPositiveClickListener)
                    .setNegativeButton("NO", onNegativeClickListener)
                    .create();

            alertDialog.show();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.nav_list) {
            fragment = ListFragment.newInstance("");

        } else if (id == R.id.nav_scaling) {
            fragment = ScalingFragment.newInstance();

        } else if (id == R.id.nav_parsing) {

        } else if (id == R.id.nav_map) {

        }  else if (id == R.id.nav_about) {

        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
                .addToBackStack(fragment.getClass().getSimpleName()).commit();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    DialogInterface.OnClickListener onPositiveClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            getSupportFragmentManager().popBackStack();

        }
    };

    DialogInterface.OnClickListener onNegativeClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };

}
