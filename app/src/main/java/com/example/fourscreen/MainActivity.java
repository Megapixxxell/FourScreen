package com.example.fourscreen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.example.fourscreen.fragments.scaling_v2.Scalingv2Fragment;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.fourscreen.fragments.FragmentAbout;
import com.example.fourscreen.fragments.Map.MapFragment;
import com.example.fourscreen.fragments.Parsing.ui.ParsingFragment;
import com.example.fourscreen.fragments.list.IOnBackPressed;
import com.example.fourscreen.fragments.list.ListFragment;
import com.example.fourscreen.fragments.scaling.ScalingFragment;

import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        setMyLocale();
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

    private void setMyLocale() {
        mPreferences = getSharedPreferences("selectedLanguage", Context.MODE_PRIVATE);
        String language = mPreferences.getString("language", "default");
        Locale locale = new Locale(language);//Set Selected Locale
        Locale.setDefault(locale);//set new locale as default
        Configuration config = new Configuration();//get Configuration
        config.locale = locale;//set config locale as selected locale
        this.getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void changeAndSaveLocale (String language) {
        mPreferences = getSharedPreferences("selectedLanguage", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mEditor.putString("language", language);
        mEditor.apply();
        setMyLocale();
    }

    public void restartApp () {
        Intent intent=getIntent();
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.russian: {
                changeAndSaveLocale("ru");
                restartApp();
                return true;
            }
            case R.id.english: {
                changeAndSaveLocale("default");
                restartApp();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.nav_list) {
            fragment = ListFragment.newInstance("");

        } else if (id == R.id.nav_scaling) {
            fragment = Scalingv2Fragment.newInstance();

        } else if (id == R.id.nav_parsing) {
            fragment = ParsingFragment.newInstance();

        } else if (id == R.id.nav_map) {
            fragment = MapFragment.newInstance();

        } else if (id == R.id.nav_about) {
            fragment = FragmentAbout.newInstance();

        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
                .addToBackStack(fragment.getClass().getSimpleName()).commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    DialogInterface.OnClickListener onPositiveClickListener = (dialog, which) -> getSupportFragmentManager().popBackStack();

    DialogInterface.OnClickListener onNegativeClickListener = (dialog, which) -> dialog.dismiss();

}