package com.example.moviecatalogue4.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moviecatalogue4.Api.Api;
import com.example.moviecatalogue4.Model.Movie;
import com.example.moviecatalogue4.Model.MovieFavorite;
import com.example.moviecatalogue4.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;


public class DetailsMovieActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM = "extraItem";
    TextView tvTitle;
    TextView tvDescription;
    TextView tvDate;
    TextView tvLanguage;
    TextView tvPopularity;
    TextView tvVoteCount;
    TextView tvVoteAverage;
    ImageView ivPoster;
    ImageView ivBackdrop;
    RatingBar ratingBar;
    Movie movie;

    private boolean isFavorite = false;
    private Menu menuItem;
    private Realm realm;
    RealmResults<MovieFavorite> realmResults;
    private String id;
    private String title;
    private String date;
    private String description;
    private String language;
    private String poster;
    private String backdrop;
    private String voteCount;
    private double voteAverage;
    private String popularity;
    private double rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_movie);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Detail Movie");

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.white));
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.transparent));

        try {
            Realm.init(this);
            realm = Realm.getDefaultInstance();
        } catch (RealmMigrationNeededException r) {
            Realm.deleteRealm(realm.getDefaultConfiguration());
            realm = Realm.getDefaultInstance();
        }

        tvTitle = findViewById(R.id.tv_item_title);
        tvDescription = findViewById(R.id.tv_item_description);
        tvDate = findViewById(R.id.tv_item_date);
        tvLanguage = findViewById(R.id.tv_item_language);
        tvPopularity = findViewById(R.id.tv_item_popularity);
        tvVoteAverage = findViewById(R.id.tv_item_vote_average);
        tvVoteCount = findViewById(R.id.tv_item_vote_count);
        ivPoster = findViewById(R.id.img_item_poster);
        ivBackdrop = findViewById(R.id.img_backdrop);
        ratingBar = findViewById(R.id.ratingBar);

        this.movie = getIntent().getParcelableExtra("MOVIE");
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.description = movie.getDescription();
        this.date = movie.getDate();
        this.language = movie.getLanguage();
        this.poster = movie.getPoster();
        this.backdrop = movie.getBackdrop();
        this.popularity = movie.getPopularity();
        this.voteCount = movie.getVoteCount();
        this.voteAverage = movie.getVoteAverage();
        this.rating = movie.getRating();


        tvTitle.setText(movie.getTitle());
        tvDescription.setText(movie.getDescription());
        tvDate.setText(movie.getDate());
        tvLanguage.setText(movie.getLanguage());
        tvVoteCount.setText(movie.getVoteCount());
        tvVoteAverage.setText(String.valueOf(voteAverage));
        tvPopularity.setText(movie.getPopularity());
        Glide.with(this)
                .load(Api.getPoster(movie.getPoster()))
                .apply(new RequestOptions().override(600, 900))
                .into(ivPoster);
        Glide.with(this)
                .load(Api.getBackdrop(movie.getBackdrop()))
                .into(ivBackdrop);
        float rate = (float) (movie.getVoteAverage() / 10) * 5;
        ratingBar.setRating(rate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_detail_menu, menu);
        menuItem = menu;
        setFavorite();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_favorite) {
            if (isFavorite) {
                boolean delete = removeFromFavoriteMovie();
                if (delete) {
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_24dp));
                    Toast.makeText(this, R.string.delete_from_favorite, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.delete_failed, Toast.LENGTH_SHORT).show();
                }
            } else {
                isFavorite = addToFavoriteMovie();
                if (isFavorite) {
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_red_24dp));
                    Toast.makeText(this, R.string.add_to_favorite, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        }
        return false;
    }

    private void setFavorite() {
        realmResults = realm.where(MovieFavorite.class).equalTo("id", this.id).findAll();
        isFavorite = !realmResults.isEmpty();
        if (isFavorite) {
            this.menuItem.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_red_24dp));
        } else {
            this.menuItem.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_24dp));
        }
    }

    private boolean addToFavoriteMovie() {
        MovieFavorite movieFavorite = new MovieFavorite();
        movieFavorite.setId(this.id);
        movieFavorite.setTitle(this.title);
        movieFavorite.setDescription(this.description);
        movieFavorite.setDate(this.date);
        movieFavorite.setLanguage(this.language);
        movieFavorite.setPoster(this.poster);
        movieFavorite.setBackdrop(this.backdrop);
        movieFavorite.setPopularity(this.popularity);
        movieFavorite.setVoteAverage(this.voteAverage);
        movieFavorite.setVoteCount(this.voteCount);
        movieFavorite.setRating(this.rating);

        realm = Realm.getDefaultInstance();
        MovieFavorite puppies = realm.where(MovieFavorite.class).equalTo("id", this.id).findFirst();
        if (puppies == null) {
            try {
                realm.beginTransaction();
                realm.copyToRealm(movieFavorite);
                realm.commitTransaction();
                realm.close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            try {
                realm.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    private boolean removeFromFavoriteMovie() {
        try {
            realm.beginTransaction();
            MovieFavorite movieFavorite = realm.where(MovieFavorite.class).equalTo("id", this.id).findFirst();
            if (movieFavorite != null) {
                movieFavorite.deleteFromRealm();
                realm.commitTransaction();
                while (realm.isInTransaction()) {
                    Log.e("Realm", "still in transaction");
                }
                realm.close();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try {
            realm.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
