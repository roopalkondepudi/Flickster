package com.example.roopalk.flickster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.roopalk.flickster.models.Config;
import com.example.roopalk.flickster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.example.roopalk.flickster.MovieListActivity.API_BASE_URL;
import static com.example.roopalk.flickster.MovieListActivity.API_KEY_PARAM;


public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;
    Config config;
//    TextView tvTitle;
//    TextView tvOverview;
//    RatingBar rbVoteAverage;

    public final static String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvOverview) TextView tvOverview;
    @BindView(R.id.rbVoteAverage) RatingBar rbVoteAverage;
    @BindView(R.id.ivPosterImage) ImageView ivPosterImage;
    String key;
//    @BindView(R.id.backdropimage) ImageView backdropimage;
    AsyncHttpClient client;

    public static final String TAG = "MovieDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        //initialize all the values
//        tvTitle = (TextView) findViewById(R.id.tvTitle);
//        tvOverview = (TextView) findViewById(R.id.tvOverview);
//        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);

        client = new AsyncHttpClient();

        //unwrap movie passed in via intent
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        config = (Config) Parcels.unwrap(getIntent().getParcelableExtra("image"));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        //set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        //set the rating
        float voteAvg = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAvg > 0 ? voteAvg / 2.0f : voteAvg);

        //load image using GLIDe
        GlideApp.with(this)
                .load(config.getImageURL(config.getPosterSize(), movie.getPosterPath()))
                .transform(new RoundedCornersTransformation(15, 0))
                .placeholder(R.drawable.flicks_movie_placeholder)
                .error(R.drawable.flicks_movie_placeholder)
                .into(ivPosterImage);
    }

    @OnClick(R.id.ivPosterImage)
    public void onClick(View view)
    {
        getTrailerURL(movie);
    }

    public void getTrailerURL(final Movie movie)
    {
        //generate the trailer activity
        String URL = API_BASE_URL + "/movie/" + movie.getId() + "/videos";

        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));

        //execute a GET request expecting a JSON Array request
        client.get(URL, params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //load the results into movies list
                try {
                    JSONArray results = response.getJSONArray("results");
                    if(results.getJSONObject(0) != null)
                    {
                        key = results.getJSONObject(0).getString("key");
                        Intent intent = new Intent(getBaseContext(), MovieTrailerActivity.class);
                        intent.putExtra("key", Parcels.wrap(key));
                        startActivity(intent);
                    }
                    Log.i(TAG, String.format("Loaded trailer of this movie: %s", movie.getTitle()));
                }
                catch (JSONException e) {
                    Log.e(TAG,"Failed to get trailer of this movie");
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, "Failed to get data from videos endpoint");
            }
        });
    }
}
