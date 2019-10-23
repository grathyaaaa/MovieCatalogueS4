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
import com.example.moviecatalogue4.Adapter.ListTvShowAdapter;
import com.example.moviecatalogue4.Network.NetworkUtils;
import com.example.moviecatalogue4.R;
import com.example.moviecatalogue4.Model.TvShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFragment extends Fragment implements SearchView.OnQueryTextListener {
    private RecyclerView recyclerViewTvShow;
    private ProgressBar progressBarTvShow;
    private ArrayList<TvShow> listTvShow;
    private ArrayList<TvShow> tempTvShow;
    private ListTvShowAdapter listTvShowAdapter;
    private SearchView searchView;

    public TvShowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tv_show, container, false);
        recyclerViewTvShow = view.findViewById(R.id.rv_tv_show);
        progressBarTvShow = view.findViewById(R.id.pb_main2);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Search...");
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        listTvShow = new ArrayList<>();
        tempTvShow = new ArrayList<>();

        recyclerViewTvShow.setLayoutManager(new LinearLayoutManager(getActivity()));
        listTvShowAdapter = new ListTvShowAdapter(getActivity());
        recyclerViewTvShow.setAdapter(listTvShowAdapter);

        listTvShowAdapter.setListTvShow(listTvShow);

        if (savedInstanceState == null) {
            loadData();
        } else {
            listTvShow = savedInstanceState.getParcelableArrayList("list");
            if (listTvShow != null) {
                listTvShowAdapter.setListTvShow(listTvShow);
            }
        }
        showRecyclerList();
    }

    private void showRecyclerList() {
        recyclerViewTvShow.setLayoutManager(new LinearLayoutManager(getActivity()));
        listTvShowAdapter = new ListTvShowAdapter(getActivity());
        listTvShowAdapter.setListTvShow(listTvShow);
        recyclerViewTvShow.setAdapter(listTvShowAdapter);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("list", listTvShowAdapter.getListTvShow());

    }

    private void loadData(){
        URL url = Api.getTvShow();
        new TvShowFragment.TvShowAsyncTask().execute(url);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.isEmpty()){
            Log.d("query", newText);
            listTvShow.clear();
            new TvShowAsyncTask().execute(Api.searchTvShow(newText));
        } else {
            listTvShow.clear();
            new TvShowAsyncTask().execute(Api.getTvShow());
        }
        listTvShowAdapter.setListTvShow(listTvShow);
        return true;
    }

    private class TvShowAsyncTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarTvShow.setVisibility(View.VISIBLE);
            recyclerViewTvShow.setVisibility(View.GONE);
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
            recyclerViewTvShow.setVisibility(View.VISIBLE);
            progressBarTvShow.setVisibility(View.GONE);
            Log.e("data now", s);

            try {
                tempTvShow.clear();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    TvShow tvShow = new TvShow(object);
                    listTvShow.add(tvShow);
                }
                tempTvShow.addAll(listTvShow);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listTvShowAdapter.setListTvShow(listTvShow);
        }
    }
}
