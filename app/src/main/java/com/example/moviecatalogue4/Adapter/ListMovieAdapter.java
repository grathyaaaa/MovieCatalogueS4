package com.example.moviecatalogue4.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moviecatalogue4.Activity.DetailsMovieActivity;
import com.example.moviecatalogue4.Api.Api;
import com.example.moviecatalogue4.Model.Movie;
import com.example.moviecatalogue4.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListMovieAdapter extends RecyclerView.Adapter<ListMovieAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Movie> listMovie;

    public ListMovieAdapter(Context context) {
        this.context = context;
    }

    private Context getContext() {
        return context;
    }

    public ArrayList<Movie> getListMovie() {
        return listMovie;
    }

    public void setListMovie(ArrayList<Movie> listMovie) {
        this.listMovie = listMovie;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListMovieAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.txtTitle.setText(getListMovie().get(position).getTitle());
        viewHolder.txtDescription.setText(getListMovie().get(position).getDescription());
        viewHolder.txtDate.setText(getListMovie().get(position).getDate());
        String posterPath = getListMovie().get(position).getPoster();
        Glide.with(context).load(Api.getPoster(posterPath))
                .apply(new RequestOptions().override(70, 100))
                .into(viewHolder.imgPoster);


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DetailsMovieActivity.class);
                intent.putExtra("MOVIE", getListMovie().get(position));
                Log.e("Title", getListMovie().get(position).getTitle());
                getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListMovie().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        TextView txtDescription;
        TextView txtDate;
        ImageView imgPoster;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtDescription = itemView.findViewById(R.id.txt_description);
            txtDate = itemView.findViewById(R.id.txt_date);
            imgPoster = itemView.findViewById(R.id.img_poster);
        }
    }
    public interface OnItemClickCallback {
        void onClicked(View v, Movie item, int position);
    }
}
