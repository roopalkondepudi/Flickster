package com.example.roopalk.flickster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.roopalk.flickster.models.Config;
import com.example.roopalk.flickster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {

    //constants
    //base URL for API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    //parameter name for the API key
    public final static String API_KEY_PARAM = "api_key";
    //tag for logging from this activity
    public final static String TAG = "MoveListActivity";

    //declare the client
    AsyncHttpClient client;
    //the list of currently playing movies
    ArrayList<Movie> movies;
    //recycler view
    RecyclerView recycleView;
    //the adapter wired to recycler view
    MovieAdapter adapter;

    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        //initialize the client
        client = new AsyncHttpClient();
        //initialize the list of movies
        movies = new ArrayList<>();
        //initialize the adapter
        adapter = new MovieAdapter(movies);
        //resolve recycler view
        recycleView = (RecyclerView) findViewById(R.id.movieList);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setAdapter(adapter);
        //get the configuration
        getConfiguration();
    }

    //get the list of currently playing movies from API
    private void getNowPlaying()
    {
        //create URL
        String url = API_BASE_URL + "/movie/now_playing";

        //set request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));

        //execute a GET request expecting a JSON object request
        client.get(url, params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //load the results into movies list
                try {
                    JSONArray results = response.getJSONArray("results");
                    //iterate through result set and create Movie objects
                    for(int i = 0; i < results.length(); i++)
                    {
                        Movie m = new Movie(results.getJSONObject(i));
                        movies.add(m);
                        adapter.notifyItemInserted(movies.size()-1);
                    }
                    Log.i(TAG, String.format("Loaded %s movies", results.length()));
                } catch (JSONException e) {
                    logError("Failed to parse now playing movies", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from now playing endpoint", throwable, true);
            }
        });

    }

    //get configuration from API
    private void getConfiguration() {
        //make the URL
        String url = API_BASE_URL + "/configuration";

        //set request parameters - parameters that get appended to the URL
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); //API key is always required

        //execute a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    config = new Config(response);
                    Log.i(TAG, String.format("Loaded configuration with imageBaseURL %s and posterSize %s", config.getBaseUrl(), config.getPosterSize()));
                    getNowPlaying();
                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                logError("Failed getting configuration", throwable, true);
            }
        });
    }
    //handle errors, log and alert user
    private void logError(String message, Throwable error, boolean alertUser)
    {
        //log the error
        Log.e(TAG, message, error);

        //alert the user to avoid silent errors
        if(alertUser)
        {
            //show long toast with error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}


//3 pieces: secure base URL, then poster size (w342), then actual path - movie's endpoint