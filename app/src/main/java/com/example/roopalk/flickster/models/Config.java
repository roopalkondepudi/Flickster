package com.example.roopalk.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by roopalk on 6/27/18.
 */

public class Config {
    //base URL for loading images
    String baseUrl;
    //poser size, part of URL
    String posterSize;

    public Config(JSONObject object) throws JSONException {
        JSONObject images = object.getJSONObject("images");
        //get image base uRL
        baseUrl = images.getString(("secure_base_url"));
        //get poster size
        JSONArray poster_sizes = images.getJSONArray("poster_sizes");
        //use option at index 3 or w432 as a fallback
        posterSize = poster_sizes.optString(3, "w432");
    }

    //helper method for creating urls
    public String getImageURL(String size, String path)
    {
        return String.format("%s%s%s", baseUrl, size, path); //concatenate all three
    }
    public String getBaseUrl() {
        return baseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }
}
