package com.example.moviecatalogue4.Api;

import android.net.Uri;

import com.example.moviecatalogue4.BuildConfig;

import java.net.MalformedURLException;
import java.net.URL;

public class Api {
    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String BASE_URL = BuildConfig.BASE_URL;
    private static final String BASE_URL_POSTER = "https://image.tmdb.org/t/p/";
    private static final String KEY = "api_key";
    private static final String SIZE = "w185";
    private static final String DISCOVER = "discover";
    private static final String SEARCH = "search";
    private static final String MOVIE = "movie";


    public static URL getMovie() {
        //https://api.themoviedb.org/3/discover/movie?api_key=<<api_key>>&language=en-US
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(DISCOVER)
                .appendPath(MOVIE)
                .appendQueryParameter(KEY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    public static URL searchMovie(String query) {
        //https://api.themoviedb.org/3/search/movie?api_key=<<api_key>>&language=en-US&query=<<query>>
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(SEARCH)
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
    public static URL getTvShow() {
        //https://api.themoviedb.org/3/discover/tv?api_key=<<api_key>>&language=en-US
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(DISCOVER)
                .appendPath("tv")
                .appendQueryParameter(KEY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL getPoster(String path) {
        //  https://image.tmdb.org/t/p/w185/zfE0R94v1E8cuKAerbskfD3VfUt.jpg
        path = path.startsWith("/") ? path.substring(1) : path;
        Uri uri = Uri.parse(BASE_URL_POSTER).buildUpon()
                .appendPath(SIZE)
                .appendPath(path)
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
