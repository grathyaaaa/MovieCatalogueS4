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

import com.example.moviecatalogue4.Adapter.ListTvShowAdapter;
import com.example.moviecatalogue4.R;
import com.example.moviecatalogue4.Model.TvShow;
import com.example.moviecatalogue4.Model.TvShowFavorite;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFavoriteFragment extends Fragment {

    private RecyclerView recyclerViewFav;
    private ProgressBar progressBarFav;
    private ArrayList<TvShow> tvShowArrayList;
    private ListTvShowAdapter listTvShowAdapter;
    private RealmResults<TvShowFavorite> tvShowFavorites;
    private Realm realm;

    public TvShowFavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv_show_favorite, container, false);
        recyclerViewFav = view.findViewById(R.id.rv_tvshow_fav);
        progressBarFav = view.findViewById(R.id.pb_tvshow_fav);
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

        tvShowArrayList = new ArrayList<>();
        showRecyclerList();
        loadData();
    }

    private void loadData() {
        tvShowFavorites = realm.where(TvShowFavorite.class).findAll();
        progressBarFav.setVisibility(View.VISIBLE);
        if (!tvShowFavorites.isEmpty()) {
            for (int i = 0; i < tvShowFavorites.size(); i++) {
                TvShow dummy = new TvShow();
                dummy.setId(tvShowFavorites.get(i).getId());
                dummy.setName(tvShowFavorites.get(i).getTitle());
                dummy.setOverview(tvShowFavorites.get(i).getDescription());
                dummy.setRelease_date(tvShowFavorites.get(i).getDate());
                dummy.setOriginal_language(tvShowFavorites.get(i).getLanguage());
                dummy.setPoster(tvShowFavorites.get(i).getPoster());
                tvShowArrayList.add(dummy);
            }
        }
        progressBarFav.setVisibility(View.GONE);
        listTvShowAdapter.setListTvShow(tvShowArrayList);
    }

    private void showRecyclerList() {
        listTvShowAdapter = new ListTvShowAdapter(getActivity());
        listTvShowAdapter.setListTvShow(tvShowArrayList);
        recyclerViewFav.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewFav.setAdapter(listTvShowAdapter);
        tvShowFavorites = realm.where(TvShowFavorite.class).findAll();
    }
}
