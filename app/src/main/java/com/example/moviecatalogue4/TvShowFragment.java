package com.example.moviecatalogue4;


import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFragment extends Fragment {
    private RecyclerView recyclerViewTvShow;
    private ProgressBar progressBarTvShow;
    private ArrayList<TvShow> listTvShow = new ArrayList<>();
    private ListTvShowAdapter listTvShowAdapter;

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listTvShow = new ArrayList<>();

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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("list", listTvShowAdapter.getListTvShow());

    }

    private void loadData(){
        URL url = Api.getTvShow();
        Log.e("url", url.toString());
        new TvShowFragment.TvShowAsyncTask().execute(url);
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
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    TvShow tvShow = new TvShow(object);
                    listTvShow.add(tvShow);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void showRecyclerList() {
        recyclerViewTvShow.setLayoutManager(new LinearLayoutManager(getActivity()));
        listTvShowAdapter = new ListTvShowAdapter(getActivity());
        listTvShowAdapter.setListTvShow(listTvShow);
        recyclerViewTvShow.setAdapter(listTvShowAdapter);
    }
}
