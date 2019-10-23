package com.example.moviecatalogue4.Widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.moviecatalogue4.Api.Api;
import com.example.moviecatalogue4.Model.Movie;
import com.example.moviecatalogue4.Model.MovieFavorite;
import com.example.moviecatalogue4.Model.TvShowFavorite;
import com.example.moviecatalogue4.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;

public class FavoriteRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<Movie> list;
    private Context context;
    private RealmResults<MovieFavorite> movieFavorites;
    private Realm realm;

    FavoriteRemoteViewsFactory(Context context1) {
        context = context1;
        list = new ArrayList<>();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        try {
            Realm.init(context);
            realm = Realm.getDefaultInstance();
        } catch (RealmMigrationNeededException e) {
            Realm.deleteRealm(realm.getDefaultConfiguration());
            realm = Realm.getDefaultInstance();
        }

        movieFavorites = realm.where(MovieFavorite.class).findAll();
        if (!movieFavorites.isEmpty()) {
            for (int i = 0; i < movieFavorites.size(); i++) {
                Movie dummy = new Movie();
                dummy.setTitle(movieFavorites.get(i).getTitle());
                dummy.setBackdrop(movieFavorites.get(i).getBackdrop());
                list.add(dummy);
            }
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        try {
            Realm.init(context);
            realm = Realm.getDefaultInstance();
        } catch (RealmMigrationNeededException e) {
            Realm.deleteRealm(realm.getDefaultConfiguration());
            realm = Realm.getDefaultInstance();
        }

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        Bitmap bitmap = null;
        String backdrop = list.get(position).getBackdrop();
        String title = list.get(position).getTitle();

        Log.d("Widget Load",backdrop);

        if (list.size() > 0) {
            try {
                bitmap = Glide.with(context).
                        asBitmap().
                        load(Api.getBackdrop(backdrop)).
                        into(1920, 1080).get();
                Log.d("Widget Load","success");
            } catch (InterruptedException | ExecutionException e) {
                Log.d("Widget Load", "error");
            }
            remoteViews.setImageViewBitmap(R.id.fav_img, bitmap);
            remoteViews.setTextViewText(R.id.fav_title, title);
        }

        Bundle extras = new Bundle();
        extras.putInt(FavoriteMovieWidget.EXTRA_ITEM, position);

        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        remoteViews.setOnClickFillInIntent(R.id.fav_img, fillInIntent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
