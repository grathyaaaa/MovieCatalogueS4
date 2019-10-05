package com.example.moviecatalogue4.Activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

import com.example.moviecatalogue4.Api.Api;
import com.example.moviecatalogue4.reminder.DailyAlarmReceiver;
import com.example.moviecatalogue4.Model.Movie;
import com.example.moviecatalogue4.Network.NetworkUtils;
import com.example.moviecatalogue4.R;
import com.example.moviecatalogue4.reminder.ReleaseAlarmReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.ActionBar;

public class SetReminderActivity extends AppCompatPreferenceActivity {
    private static DailyAlarmReceiver dailyAlarmReceiver;
    private static ReleaseAlarmReceiver releaseAlarmReceiver;
    private static Context context;
    private static List<Movie> listMovie;

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            boolean booleanValue = (boolean) value;

            String key = preference.getKey();
            String daily = "daily_remainder";
            String release = "release_remainder";
            if (key.equals(daily) ) {
                if(booleanValue) {
                    dailyAlarmReceiver.setRepeatingAlarm(getAppContext());
                } else {
                    dailyAlarmReceiver.cancelAlarm(getAppContext());
                }
            }

            if (key.equals(release) ) {
                if(booleanValue) {
                    setRepeatingAlarm();
                } else {
                    releaseAlarmReceiver.cancelAlarm(getAppContext());
                }
            }
            return true;
        }
    };

    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                        .getBoolean(preference.getKey(), false));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();

        dailyAlarmReceiver = new DailyAlarmReceiver();
        releaseAlarmReceiver = new ReleaseAlarmReceiver();
        context = getApplicationContext();

        listMovie = new ArrayList<>();

        this.getFragmentManager().beginTransaction().replace(android.R.id.content , new RemainderPreferenceFragment()).commit();
    }

    public static Context getAppContext(){
        return context;
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || RemainderPreferenceFragment.class.getName().equals(fragmentName);
    }

    public static void setAlarm(List<Movie> movies) {
        releaseAlarmReceiver.setRepeatingAlarm(getAppContext(), movies);
    }

    public static void setRepeatingAlarm() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String currentDate = dateFormat.format(date);
//        String currentDate = "2019-02-07";
        Log.e("currentDate", currentDate);

        URL url = Api.getUpComingMovie();
        new MovieAsyncTask(currentDate).execute(url);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class RemainderPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.root_preferences);

            Preference preference = findPreference("daily_remainder");
            boolean isOn = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(preference.getKey(), false);

            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_daily_remainder)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_release_remainder)));

            Log.e("onCreate", String.valueOf(isOn));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case android.R.id.home :
                    startActivity(new Intent(getActivity(), SetReminderActivity.class));
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    private static class MovieAsyncTask extends AsyncTask<URL, Void, String> {
        String currentDate;
        private MovieAsyncTask(String currentDate) {
            this.currentDate = currentDate;
        }
        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String result = null;
            try {
                result = NetworkUtils.getFromNetwork(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("data up", s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for (int i=0; i<jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    Movie movie = new Movie(object);
                    if(movie.getReleaseDate().equals(currentDate)){
                        listMovie.add(movie);
                        Log.e("release date", movie.getReleaseDate());
                    }
                }
                setAlarm(listMovie);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
