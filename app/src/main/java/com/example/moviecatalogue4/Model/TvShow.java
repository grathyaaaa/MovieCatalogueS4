package com.example.moviecatalogue4.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class TvShow implements Parcelable {
    private String id;
    private String name;
    private String overview;
    private String release_date;
    private String original_language;
    private String poster;
    private String backdrop;

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

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    private String popularity;
    private String voteCount;
    private double voteAverage;
    private double rating;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public TvShow(Parcel in) {
        id = in.readString();
        name = in.readString();
        overview = in.readString();
        release_date = in.readString();
        original_language = in.readString();
        poster = in.readString();
        backdrop = in.readString();
        popularity = in.readString();
        voteAverage = in.readDouble();
        voteCount = in.readString();
        rating = in.readDouble();
    }

    public TvShow() {
    }

    public static final Creator<TvShow> CREATOR = new Creator<TvShow>() {
        @Override
        public TvShow createFromParcel(Parcel source) {
            return new TvShow(source);
        }

        @Override
        public TvShow[] newArray(int size) {
            return new TvShow[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeString(original_language);
        dest.writeString(poster);
        dest.writeString(backdrop);
        dest.writeString(voteCount);
        dest.writeDouble(voteAverage);
        dest.writeString(popularity);
        dest.writeDouble(rating);
    }

    public TvShow(JSONObject object) {
        try {
            id = object.getString("id");
            name = object.getString("name");
            overview = object.getString("overview");
            release_date = object.getString("first_air_date");
            original_language = object.getString("original_language");
            poster = object.getString("poster_path");
            backdrop = object.getString("backdrop_path");
            popularity = object.getString("popularity");
            voteCount = object.getString("vote_count");
            voteAverage = object.getDouble("vote_average");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
