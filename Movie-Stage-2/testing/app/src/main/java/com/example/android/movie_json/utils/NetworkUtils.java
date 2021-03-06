package com.example.android.movie_json.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.example.android.movie_json.BuildConfig;
import com.example.android.movie_json.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    final static String  MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie";

    final static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p";

    final static String API_KEY = BuildConfig.API_KEY;

    final static String PARAM_KEY = "api_key";

    final static String PARAM_PAGE = "page";

    final static String IMAGE_SIZE = "w342";

    final static String VIDEO_MEANS = "videos";

    final static String REVIEW_MEANS = "reviews";

    public static URL buildUrl(String means,int pageNo){
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                          .appendPath(means)
                          .appendQueryParameter(PARAM_KEY, API_KEY)
                          .appendQueryParameter(PARAM_PAGE,String.valueOf(pageNo))
                          .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildUrl(String means){
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(means)
                .appendQueryParameter(PARAM_KEY,API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static URL getImageUrl(String mPosterPath){

        Uri builtUri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(IMAGE_SIZE)
                .appendPath(mPosterPath.substring(1))
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL getVideoUrl(int id){
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendPath(String.valueOf(id))
                        .appendPath(VIDEO_MEANS)
                        .appendQueryParameter(PARAM_KEY,API_KEY)
                        .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL getReviewUrl(int id){
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(String.valueOf(id))
                .appendPath(REVIEW_MEANS)
                .appendQueryParameter(PARAM_KEY,API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }



}
