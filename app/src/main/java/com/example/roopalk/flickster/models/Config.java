package com.example.roopalk.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by roopalk on 6/27/18.
 */

@Parcel
public class Config {
    //base URL for loading images
    String baseUrl;
    //poser size, part of URL
    String posterSize;
    //backdrop size
    String backdropSize;

    public Config()
    {

    }

    public Config(JSONObject object) throws JSONException {
        JSONObject images = object.getJSONObject("images");
        //get image base uRL
        baseUrl = images.getString(("secure_base_url"));
        //get poster size
        JSONArray poster_sizes = images.getJSONArray("poster_sizes");
        //use option at index 3 or w432 as a fallback
        posterSize = poster_sizes.optString(3, "w432");
        //parse backdrop size and use option at index 1 or use w780 as a fallback
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        backdropSize=backdropSizeOptions.optString(1, "w780");

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

    public String getBackdropSize() {
        return backdropSize;
    }
}
