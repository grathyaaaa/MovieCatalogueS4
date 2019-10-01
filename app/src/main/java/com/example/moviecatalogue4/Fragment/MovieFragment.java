package com.example.moviecatalogue4.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.example.moviecatalogue4.Api.Api;
import com.example.moviecatalogue4.Adapter.ListMovieAdapter;
import com.example.moviecatalogue4.Model.Movie;
import com.example.moviecatalogue4.Network.NetworkUtils;
import com.example.moviecatalogue4.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment implements SearchView.OnQueryTextListener{
    private RecyclerView recyclerViewMovie;
    private ProgressBar progressBarMovie;
    private ArrayList<Movie> listMovies;
    private ArrayList<Movie> tempMovie;
    private ListMovieAdapter listMovieAdapter;
    private SearchView searchView;

    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        recyclerViewMovie = view.findViewById(R.id.rv_movies);
        progressBarMovie = view.findViewById(R.id.pb_main);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        searchView = (SearchView)  menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Search...");
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        listMovies = new ArrayList<>();
        tempMovie = new ArrayList<>();

        recyclerViewMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        listMovieAdapter = new ListMovieAdapter(getActivity());
        recyclerViewMovie.setAdapter(listMovieAdapter);

        listMovieAdapter.setListMovie(listMovies);

        if (savedInstanceState == null) {
            loadData();
        } else {
            listMovies = savedInstanceState.getParcelableArrayList("list");
            if (listMovies != null) {
                listMovieAdapter.setListMovie(listMovies);
            }
        }
        showRecyclerList();
    }

    private void showRecyclerList() {
        recyclerViewMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        listMovieAdapter = new ListMovieAdapter(getActivity());
        listMovieAdapter.setListMovie(listMovies);
        recyclerViewMovie.setAdapter(listMovieAdapter);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("list", listMovies);
    }

    private void loadData() {
        URL url = Api.getMovie();
        new MovieAsyncTask().execute(url);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.isEmpty()){
            Log.d("query",newText);
            listMovies.clear();
            new MovieAsyncTask().execute(Api.getMovie(newText));
        } else {
            listMovies.clear();
            listMovies.addAll(tempMovie);
        }
        listMovieAdapter.setListMovie(listMovies);
        return true;
    }
    private class MovieAsyncTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarMovie.setVisibility(View.VISIBLE);
            recyclerViewMovie.setVisibility(View.GONE);
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
            recyclerViewMovie.setVisibility(View.VISIBLE);
            progressBarMovie.setVisibility(View.GONE);
            Log.d("data now", s);

            try {
                tempMovie.clear();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    Movie movie = new Movie(object);
                    listMovies.add(movie);
                }
                tempMovie.addAll(listMovies);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listMovieAdapter.setListMovie(listMovies);
        }

    }

}
