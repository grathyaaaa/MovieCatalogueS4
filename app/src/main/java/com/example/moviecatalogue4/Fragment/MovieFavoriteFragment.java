package com.example.moviecatalogue4.Fragment;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.moviecatalogue4.Adapter.ListMovieAdapter;
import com.example.moviecatalogue4.Model.Movie;
import com.example.moviecatalogue4.Model.MovieFavorite;
import com.example.moviecatalogue4.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFavoriteFragment extends Fragment {
    private RecyclerView recyclerViewFav;
    private ProgressBar progressBarFav;
    private ArrayList<Movie> movieArrayList;
    private ListMovieAdapter listMovieAdapter;
    private RealmResults<MovieFavorite> movieFavorites;
    public static Realm realm;

    public MovieFavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_favorite, container, false);
        recyclerViewFav = view.findViewById(R.id.rv_movie_fav);
        progressBarFav = view.findViewById(R.id.pb_movie_fav);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            Realm.init(getActivity());
            realm = Realm.getDefaultInstance();
        } catch (RealmMigrationNeededException e) {
            Realm.deleteRealm(realm.getDefaultConfiguration());
            realm = Realm.getDefaultInstance();
        }

        movieArrayList = new ArrayList<>();
        showRecyclerList();
        loadData();
    }

    private void loadData() {
        movieFavorites = realm.where(MovieFavorite.class).findAll();
        progressBarFav.setVisibility(View.VISIBLE);
        if (!movieFavorites.isEmpty()) {
            for (int i = 0; i < movieFavorites.size(); i++) {
                Movie dummy = new Movie();
                dummy.setId(movieFavorites.get(i).getId());
                dummy.setTitle(movieFavorites.get(i).getTitle());
                dummy.setDescription(movieFavorites.get(i).getDescription());
                dummy.setDate(movieFavorites.get(i).getDate());
                dummy.setLanguage(movieFavorites.get(i).getLanguage());
                dummy.setPoster(movieFavorites.get(i).getPoster());
                dummy.setBackdrop(movieFavorites.get(i).getBackdrop());
                dummy.setPopularity(movieFavorites.get(i).getPopularity());
                dummy.setRating(movieFavorites.get(i).getRating());
                dummy.setVoteAverage(movieFavorites.get(i).getVoteAverage());
                dummy.setVoteCount(movieFavorites.get(i).getVoteCount());
                movieArrayList.add(dummy);
            }
        }
        progressBarFav.setVisibility(View.GONE);
        listMovieAdapter.setListMovie(movieArrayList);
    }

    private void showRecyclerList() {
        listMovieAdapter = new ListMovieAdapter(getActivity());
        listMovieAdapter.setListMovie(movieArrayList);
        recyclerViewFav.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewFav.setAdapter(listMovieAdapter);
        movieFavorites = realm.where(MovieFavorite.class).findAll();
    }
}
