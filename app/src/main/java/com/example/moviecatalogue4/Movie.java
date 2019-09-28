package com.example.moviecatalogue4;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Movie implements Parcelable {
    private String id;
    private String title;
    private String description;
    private String date;
    private String language;
    private String poster;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Movie() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(date);
        dest.writeString(description);
        dest.writeString(poster);
        dest.writeString(language);
    }

    public Movie(Parcel in) {
        id = in.readString();
        title = in.readString();
        date = in.readString();
        description = in.readString();
        poster = in.readString();
        language = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie(JSONObject object) {
        try {
            id = object.getString("id");
            title = object.getString("title");
            date = object.getString("release_date");
            description = object.getString("overview");
            poster = object.getString("poster_path");
            language = object.getString("original_language");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
