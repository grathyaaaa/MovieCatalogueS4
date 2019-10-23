package com.example.moviecatalogue4.Model;

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
    private String backdrop;
    private String popularity;
    private String voteCount;
    private double voteAverage;
    private double rating;

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

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
        dest.writeString(backdrop);
        dest.writeString(voteCount);
        dest.writeDouble(voteAverage);
        dest.writeString(popularity);
        dest.writeDouble(rating);
    }

    public Movie(Parcel in) {
        id = in.readString();
        title = in.readString();
        date = in.readString();
        description = in.readString();
        poster = in.readString();
        language = in.readString();
        backdrop = in.readString();
        popularity = in.readString();
        voteAverage = in.readDouble();
        voteCount = in.readString();
        rating = in.readDouble();
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
            backdrop = object.getString("backdrop_path");
            popularity = object.getString("popularity");
            voteCount = object.getString("vote_count");
            voteAverage = object.getDouble("vote_average");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
