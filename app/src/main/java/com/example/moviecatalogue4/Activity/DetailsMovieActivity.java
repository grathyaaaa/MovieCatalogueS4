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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moviecatalogue4.Api.Api;
import com.example.moviecatalogue4.Model.Movie;
import com.example.moviecatalogue4.Model.MovieFavorite;
import com.example.moviecatalogue4.R;


public class DetailsMovieActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM = "extraItem";
    TextView tvTitle;
    TextView tvDescription;
    TextView tvDate;
    TextView tvLanguage;
    ImageView ivPoster;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_movie);

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
        ivPoster = findViewById(R.id.img_item_poster);

        this.movie = getIntent().getParcelableExtra("MOVIE");
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.description = movie.getDescription();
        this.date = movie.getDate();
        this.language = movie.getLanguage();
        this.poster = movie.getPoster();

//        item = getIntent().getParcelableExtra(EXTRA_ITEM);

        tvTitle.setText(movie.getTitle());
        tvDescription.setText(movie.getDescription());
        tvDate.setText(movie.getDate());
        tvLanguage.setText(movie.getLanguage());
        Glide.with(this)
                .load(Api.getPoster(movie.getPoster()))
                .apply(new RequestOptions().override(600, 900))
                .into(ivPoster);
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
