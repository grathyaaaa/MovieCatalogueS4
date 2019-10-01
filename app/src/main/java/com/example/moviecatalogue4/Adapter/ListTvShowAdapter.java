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
import com.example.moviecatalogue4.Activity.DetailsTvShowActivity;
import com.example.moviecatalogue4.Api.Api;
import com.example.moviecatalogue4.R;
import com.example.moviecatalogue4.Model.TvShow;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListTvShowAdapter extends RecyclerView.Adapter <ListTvShowAdapter.TvShowViewHolder> {

    private Context context;
    private ArrayList<TvShow> listTvShow;

    public Context getContext() {
        return context;
    }

    public ListTvShowAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<TvShow> getListTvShow() {
        return listTvShow;
    }

    public void setListTvShow(ArrayList<TvShow> listTvShow) {
        this.listTvShow = listTvShow;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TvShowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list,viewGroup,false);
        return new TvShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListTvShowAdapter.TvShowViewHolder ViewHolder, final int position) {
        ViewHolder.txtName.setText(getListTvShow().get(position).getName());
        ViewHolder.txtOverview.setText(getListTvShow().get(position).getOverview());
        String poster_path = getListTvShow().get(position).getPoster();
        Glide.with(context).load(Api.getPoster(poster_path))
                .apply(new RequestOptions().override(70, 100))
                .into(ViewHolder.imgPoster);

        ViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DetailsTvShowActivity.class);
                intent.putExtra("TVSHOW", getListTvShow().get(position));
                Log.e("Name", getListTvShow().get(position).getName());
                getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListTvShow().size();
    }

    class TvShowViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        TextView txtOverview;
        ImageView imgPoster;

        TvShowViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_title);
            txtOverview = itemView.findViewById(R.id.txt_description);
            imgPoster = itemView.findViewById(R.id.img_poster);
        }
    }

}
