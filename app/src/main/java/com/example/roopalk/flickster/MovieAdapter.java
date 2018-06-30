package com.example.roopalk.flickster;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.roopalk.flickster.models.Config;
import com.example.roopalk.flickster.models.Movie;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

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

    public void setConfig (Config config) {
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
        String imageURL = null;

        //determine current orientation
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        if(isPortrait)
        {
            imageURL = config.getImageURL(config.getPosterSize(), movie.getPosterPath());
        }
        else
        {
            imageURL = config.getImageURL(config.getBackdropSize(), movie.getBackdropPath());
        }

        int placeholderID = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView poster = isPortrait ? holder.posterImage :  holder.backdropImage;
        //load image using GLIDe
        GlideApp.with(context)
                .load(imageURL)
                .transform(new RoundedCornersTransformation(15, 0))
                .placeholder(placeholderID)
                .error(placeholderID)
                .into(poster);
    }

    public int getItemCount()
    {
        return movies.size();
    }

    //create the viewholder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        //track view objects
        @Nullable @BindView(R.id.movieImage) ImageView posterImage;
        @BindView(R.id.movieTitle) TextView movieTitle;
        @BindView(R.id.movieOverview) TextView movieOverview;
        @Nullable @BindView(R.id.backdropimage) ImageView backdropImage;


        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            posterImage = (ImageView) itemView.findViewById(R.id.movieImage);
//            movieOverview = (TextView) itemView.findViewById(R.id.movieOverview);
//            movieTitle = (TextView) itemView.findViewById(R.id.movieTitle);
//            backdropImage = (ImageView) itemView.findViewById(R.id.backdropimage);

            itemView.setOnClickListener(this);

            /* I tried to do this UI but it turns out that it isn't the best way to view the app
            //setting the scrollView
            movieOverview.setMovementMethod(new ScrollingMovementMethod());
            */
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            //check if position is valid
            if(position != RecyclerView.NO_POSITION)
            {
                //get movie @ position
                Movie movie = movies.get(position);

                //create intent for new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                intent.putExtra("image", Parcels.wrap(config));

                //serialize the movie using Parceler
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));

                //start activity
                context.startActivity(intent);

            }
        }
    }
}