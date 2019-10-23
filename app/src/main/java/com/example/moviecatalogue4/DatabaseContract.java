package com.example.moviecatalogue4;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String TABLE_NAME = "favorite";

    public static final class FavoriteColumns implements BaseColumns {
        public static final String _ID = "_id";
        public static final String TITLE = "title";
        public static final String DATE = "date";
        public static final String OVERVIEW = "overview";
        public static final String POSTER_PATH = "poster_path";
        public static final String RATING = "rating";
        public static final String POPULARITY = "popularity";
        public static final String LANGUAGE = "language";
        public static final String BACKDROP = "backdrop";
        public static final String VOTE_COUNT = "vote_count";
        public static final String VOTE_AVERAGE = "vote_average";
    }

    public static final String CONTENT_AUTHORITY = "com.example.moviecatalogue4";

    public static Uri CONTENT_URI = new Uri.Builder()
            .scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_NAME)
            .build();

    public static String getColumString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getCoulmnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }
}
