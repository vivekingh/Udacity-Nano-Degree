package com.example.android.movie_json.utils;

import com.example.android.movie_json.model.Movie;
import com.example.android.movie_json.model.Review;
import com.example.android.movie_json.model.Video;

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

    public static ArrayList<Video> getVideoKeyList(String jsonString){
        ArrayList<Video> videoKey = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.optJSONArray("results");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObjectNew = jsonArray.getJSONObject(i);
                videoKey.add(new Video(jsonObjectNew.optString("key"),jsonObjectNew.optString("name")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return videoKey;
    }

    public static ArrayList<Review> getReviewList(String jsonString){
        ArrayList<Review> reviews = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.optJSONArray("results");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObjectNew = jsonArray.getJSONObject(i);
                reviews.add(new Review(jsonObjectNew.optString("author"),jsonObjectNew.optString("content")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviews;
    }
}
