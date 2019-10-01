package com.example.moviecatalogue4.Model;

import android.net.Uri;
import android.util.Log;

import com.example.moviecatalogue4.BuildConfig;
import com.example.moviecatalogue4.Model.Movie;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ItemViewModel extends ViewModel {
    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String BASE_URL = BuildConfig.BASE_URL;
    private static final String KEY = "api_key";
    private static final String DISCOVER = "discover";
    private static final String MOVIE = "movie";
    public static final String ITEM_MOVIE = "movie";
    public static final String LIST_MOVIE_URL = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&language=en-US";
    public static final String SEARCH_MOVIE_URL = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&language=en-US&query=%s";
    private MutableLiveData<ArrayList<Movie>> listItem = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Movie>> getListItem() {
        return listItem;
    }
    public static URL getMovie(String query) {
        //https://api.themoviedb.org/3/discover/movie?api_key=<<api_key>>&language=en-US
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(DISCOVER)
                .appendPath(MOVIE)
                .appendQueryParameter(KEY, API_KEY)
                .appendQueryParameter("query", query)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


}
