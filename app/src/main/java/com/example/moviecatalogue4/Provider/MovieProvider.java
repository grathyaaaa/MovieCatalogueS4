package com.example.moviecatalogue4.Provider;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;

import com.example.moviecatalogue4.Service.CleanupJobService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmResults;
import io.realm.RealmSchema;
import io.realm.exceptions.RealmMigrationNeededException;

import com.example.moviecatalogue4.DatabaseContract.FavoriteColumns;
import com.example.moviecatalogue4.Model.MovieFavorite;

import static com.example.moviecatalogue4.DatabaseContract.CONTENT_AUTHORITY;

public class MovieProvider extends ContentProvider {

    private static final int MOVIE = 100;
    private static final int MOVIE_ID = 101;
    private static final int CLEANUP_JOB_ID = 43;
    private static final String TABLE_FAVORITE = "favorite";
    private static final String _ID = "_id";
    private static final String TITLE = "title";
    private static final String DATE = "date";
    private static final String OVERVIEW = "overview";
    private static final String POSTER_PATH = "poster_path";
    private static final String RATING = "rating";
    private static final String POPULARITY = "popularity";
    private static final String LANGUAGE = "language";
    private static final String BACKDROP = "backdrop";
    private static final String VOTE_COUNT = "vote_count";
    private static final String VOTE_AVERAGE = "vote_average";
    private static String TAG = MovieProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private Realm realm;

    static {
        sUriMatcher.addURI(CONTENT_AUTHORITY, TABLE_FAVORITE, MOVIE);
        sUriMatcher.addURI(CONTENT_AUTHORITY, TABLE_FAVORITE + "/#", MOVIE_ID);
    }

    @Override
    public boolean onCreate() {
        Realm.init(getContext());
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .migration(new MyRealmMigration())
                .build();
        Realm.setDefaultConfiguration(configuration);
        manageCleanupJob();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int match = sUriMatcher.match(uri);
        try {
            realm = realm.getDefaultInstance();
        } catch (RealmMigrationNeededException e) {
            if (Realm.getDefaultConfiguration() != null) {
                Realm.deleteRealm(Realm.getDefaultConfiguration());
                realm = realm.getDefaultInstance();
            }
        }

        MatrixCursor matrixCursor = new MatrixCursor(new String[]{FavoriteColumns._ID, FavoriteColumns.TITLE
                , FavoriteColumns.DATE, FavoriteColumns.OVERVIEW
                , FavoriteColumns.POSTER_PATH, FavoriteColumns.RATING
                , FavoriteColumns.POPULARITY, FavoriteColumns.LANGUAGE
                , FavoriteColumns.BACKDROP, FavoriteColumns.VOTE_COUNT
                , FavoriteColumns.VOTE_AVERAGE});

        try {
            switch (match) {
                case MOVIE:
                    RealmResults<MovieFavorite> movieFavoriteRealmResults = realm.where(MovieFavorite.class).findAll();
                    for (MovieFavorite movieFavorite : movieFavoriteRealmResults) {
                        Object[] rowData = new Object[]{movieFavorite.getId(), movieFavorite.getTitle()
                                , movieFavorite.getDescription(), movieFavorite.getDate()
                                , movieFavorite.getPoster(), movieFavorite.getRating()
                                , movieFavorite.getPopularity(), movieFavorite.getBackdrop()
                                , movieFavorite.getLanguage(), movieFavorite.getVoteCount()
                                , movieFavorite.getVoteAverage()};
                        matrixCursor.addRow(rowData);
                        Log.v("RealmDB", movieFavorite.toString());
                    }
                    break;

                case MOVIE_ID:
                    Integer id = Integer.parseInt(uri.getPathSegments().get(1));
                    MovieFavorite movieFavorite = realm.where(MovieFavorite.class).equalTo("id", id).findFirst();
                    matrixCursor.addRow(new Object[]{movieFavorite.getId(), movieFavorite.getDescription()
                            , movieFavorite.getTitle(), movieFavorite.getPoster()
                            , movieFavorite.getRating(), movieFavorite.getPopularity()
                            , movieFavorite.getLanguage(), movieFavorite.getBackdrop()
                            , movieFavorite.getVoteCount(), movieFavorite.getDate()
                            , movieFavorite.getVoteAverage()});
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
            matrixCursor.setNotificationUri(getContext().getContentResolver(), uri);
        } finally {
            realm.close();
        }
        return matrixCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    private void manageCleanupJob() {
        Log.d(TAG, "Scheduling cleanup job");
        JobScheduler jobScheduler = (JobScheduler) getContext()
                .getSystemService(Context.JOB_SCHEDULER_SERVICE);
        long jobInterval = DateUtils.HOUR_IN_MILLIS;

        ComponentName jobService = new ComponentName(getContext(), CleanupJobService.class);
        JobInfo favorite = new JobInfo.Builder(CLEANUP_JOB_ID, jobService).setPeriodic(jobInterval).setPersisted(true).build();
        if (jobScheduler.schedule(favorite) != JobScheduler.RESULT_SUCCESS) {
            Log.w(TAG, "Unable to schedule cleanup job");
        }
    }

    class MyRealmMigration implements RealmMigration {

        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            RealmSchema schema = realm.getSchema();
            if (oldVersion != 0) {
                schema.create(TABLE_FAVORITE)
                        .addField(_ID, Integer.class)
                        .addField(POSTER_PATH, String.class)
                        .addField(TITLE, String.class)
                        .addField(LANGUAGE, String.class)
                        .addField(DATE, String.class)
                        .addField(RATING, double.class)
                        .addField(OVERVIEW, String.class)
                        .addField(VOTE_AVERAGE, double.class)
                        .addField(POPULARITY, String.class)
                        .addField(VOTE_COUNT, String.class)
                        .addField(BACKDROP, String.class);
                oldVersion++;
            }
        }
    }
}
