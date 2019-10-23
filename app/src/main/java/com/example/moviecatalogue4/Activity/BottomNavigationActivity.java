package com.example.moviecatalogue4.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.moviecatalogue4.Fragment.FavoriteFragment;
import com.example.moviecatalogue4.Fragment.MovieFragment;
import com.example.moviecatalogue4.Fragment.TvShowFragment;
import com.example.moviecatalogue4.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private Fragment fragment;
    private final String TAG_FRAGMENT = "fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BottomNavigationView bottomNavigationView;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_movie);
            fragment = new MovieFragment();
        } else {
            fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
            loadFragment(fragment);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_movie:
                fragment = new MovieFragment();
                loadFragment(fragment);
                return true;
            case R.id.navigation_tv_show:
                fragment = new TvShowFragment();
                loadFragment(fragment);
                return true;
            case R.id.navigation_favorited:
                fragment = new FavoriteFragment();
                loadFragment(fragment);
                return true;
        }
        return false;
    }

    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_main, fragment, TAG_FRAGMENT)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_change_language :
                Intent intentLanguage = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intentLanguage);
            case R.id.menu_setting_reminder :
                Intent intentRemainder = new Intent(this, SetReminderActivity.class);
                startActivity(intentRemainder);
        }
        return super.onOptionsItemSelected(item);
    }

}
