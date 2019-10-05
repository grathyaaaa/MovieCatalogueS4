package com.example.moviecatalogue4.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moviecatalogue4.Api.Api;
import com.example.moviecatalogue4.R;
import com.example.moviecatalogue4.Model.TvShow;
import com.example.moviecatalogue4.Model.TvShowFavorite;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class DetailsTvShowActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM = "extraItem";
    TextView tvName;
    TextView tvOverview;
    TextView tvReleaseDate;
    TextView tvLanguage;
    TextView tvVoteCount;
    TextView tvVoteAverage;
    TextView tvPopularity;
    ImageView ivPoster;
    ImageView ivBackdrop;
    RatingBar ratingBar;
    TvShow tvShow;

    private boolean isFavorite = false;
    private Menu menuItem;
    private Realm realm;
    RealmResults<TvShowFavorite> realmResults;
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
        setContentView(R.layout.activity_details_tv_show);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Detail Tv Show");

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

        tvName = findViewById(R.id.tv_item_title);
        tvOverview = findViewById(R.id.tv_item_description);
        tvReleaseDate = findViewById(R.id.tv_item_date);
        tvLanguage = findViewById(R.id.tv_item_language);
        tvPopularity = findViewById(R.id.tv_item_popularity);
        tvVoteAverage = findViewById(R.id.tv_item_vote_average);
        tvVoteCount = findViewById(R.id.tv_item_vote_count);
        ivPoster = findViewById(R.id.img_item_poster);
        ivBackdrop = findViewById(R.id.img_backdrop);
        ratingBar = findViewById(R.id.ratingBar);


        this.tvShow = getIntent().getParcelableExtra("TVSHOW");
        this.id = tvShow.getId();
        this.title = tvShow.getName();
        this.description = tvShow.getOverview();
        this.date = tvShow.getRelease_date();
        this.language = tvShow.getOriginal_language();
        this.poster = tvShow.getPoster();
        this.backdrop = tvShow.getBackdrop();
        this.popularity = tvShow.getPopularity();
        this.voteCount = tvShow.getVoteCount();
        this.voteAverage = tvShow.getVoteAverage();
        this.rating = tvShow.getRating();

        tvName.setText(tvShow.getName());
        tvOverview.setText(tvShow.getOverview());
        tvReleaseDate.setText(tvShow.getRelease_date());
        tvLanguage.setText(tvShow.getOriginal_language());
        tvVoteCount.setText(tvShow.getVoteCount());
        tvVoteAverage.setText(String.valueOf(voteAverage));
        tvPopularity.setText(tvShow.getPopularity());
        Glide.with(this)
                .load(Api.getPoster(tvShow.getPoster()))
                .apply(new RequestOptions().override(600, 900))
                .into(ivPoster);
        Glide.with(this)
                .load(Api.getBackdrop(tvShow.getBackdrop()))
                .apply(new RequestOptions().override(1920, 1080))
                .into(ivBackdrop);
        float rate = (float) (tvShow.getVoteAverage() / 10) * 5;
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
                boolean delete = removeFromFavoriteTvShow();
                if (delete) {
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_24dp));
                    Toast.makeText(this, R.string.delete_from_favorite, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.delete_failed, Toast.LENGTH_SHORT).show();
                }
            } else {
                isFavorite = addToFavoriteTvShow();
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
        realmResults = realm.where(TvShowFavorite.class).equalTo("id", this.id).findAll();
        isFavorite = !realmResults.isEmpty();
        if (isFavorite) {
            this.menuItem.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_red_24dp));
        } else {
            this.menuItem.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_24dp));
        }
    }

    private boolean addToFavoriteTvShow () {
    TvShowFavorite tvShowFavorite = new TvShowFavorite();
        tvShowFavorite.setId(this.id);
        tvShowFavorite.setTitle(this.title);
        tvShowFavorite.setDescription(this.description);
        tvShowFavorite.setDate(this.date);
        tvShowFavorite.setLanguage(this.language);
        tvShowFavorite.setPoster(this.poster);
        tvShowFavorite.setBackdrop(this.backdrop);
        tvShowFavorite.setPopularity(this.popularity);
        tvShowFavorite.setVoteAverage(this.voteAverage);
        tvShowFavorite.setVoteCount(this.voteCount);
        tvShowFavorite.setRating(this.rating);

        realm = Realm.getDefaultInstance();
        TvShowFavorite tvShowFavorites = realm.where(TvShowFavorite.class).equalTo("id", this.id).findFirst();
        if (tvShowFavorites == null) {
            try {
                realm.beginTransaction();
                realm.copyToRealm(tvShowFavorite);
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

    private boolean removeFromFavoriteTvShow() {
        try {
            realm.beginTransaction();
            TvShowFavorite tvShowFavorite = realm.where(TvShowFavorite.class).equalTo("id", this.id).findFirst();
            if (tvShowFavorite != null) {
                tvShowFavorite.deleteFromRealm();
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
