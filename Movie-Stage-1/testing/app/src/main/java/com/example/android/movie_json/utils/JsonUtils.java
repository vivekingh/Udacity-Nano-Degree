package com.example.android.movie_json.utils;

import android.util.Log;

import com.example.android.movie_json.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {
    public static ArrayList<Movie> getMovieList(String jsonString) {

        ArrayList<Movie> moviesInfo = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.optJSONArray("results");

            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObjectNew = jsonArray.getJSONObject(i);
                moviesInfo.add(new Movie(jsonObjectNew.optInt("id"),
                        jsonObjectNew.optString("original_title"),
                        jsonObjectNew.optString("poster_path"),
                        jsonObjectNew.optString("overview"),
                        jsonObjectNew.optString("vote_average"),
                        jsonObjectNew.optString("release_date")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }



        return moviesInfo;
    }
}
