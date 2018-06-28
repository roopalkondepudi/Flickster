package com.example.roopalk.flickster;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.roopalk.flickster.models.Config;
import com.example.roopalk.flickster.models.Movie;

import java.util.ArrayList;

/**
 * Created by roopalk on 6/27/18.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>
{
    ArrayList<Movie> movies;
    Config config;
    Context context;


    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public MovieAdapter(Config config) {
        this.config = config;
    }

    //creates and inflates a new view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //get context and create the inflater
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //create view using item_movie layout
        View movieView = inflater.inflate(R.layout.movie_item, parent, false);
        //return new ViewHolder
        return new ViewHolder(movieView);
    }

    //binds inflated view to new item
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        //get movie data
        Movie movie = movies.get(position);
        //populate view
        holder.movieTitle.setText(movie.getTitle());
        holder.movieOverview.setText(movie.getOverview());
        String imageURL = config.getImageURL(config.getPosterSize(), movie.getPosterPath());

        //load image using GLIDe
        Glide.with(context)
                .load(imageURL)
                .into(holder.posterImage);
    }

    public int getItemCount()
    {
        return movies.size();
    }

    //create the viewholder
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        //track view objects
        ImageView posterImage;
        TextView movieTitle;
        TextView movieOverview;

        public ViewHolder(View itemView)
        {
            super(itemView);
            posterImage = (ImageView) itemView.findViewById(R.id.movieImage);
            movieOverview = (TextView) itemView.findViewById(R.id.movieOverview);
            movieTitle = (TextView) itemView.findViewById(R.id.movieTitle);
        }
    }
}