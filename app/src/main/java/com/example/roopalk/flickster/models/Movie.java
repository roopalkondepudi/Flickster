package com.example.roopalk.flickster.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by roopalk on 6/27/18.
 */

public class Movie
{
    private String title;
    private String overview;
    private String posterPath;

    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }
}
