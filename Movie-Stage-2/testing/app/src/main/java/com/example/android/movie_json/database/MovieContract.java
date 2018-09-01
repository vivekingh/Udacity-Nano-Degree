package com.example.android.movie_json.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String AUTHORITY = "com.example.android.movie_json";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_FAVOURITE = "favouritemovies";

    private MovieContract(){}

    public static final class MovieEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITE).build();
        public static final String TABLE_NAME = "favouritemovies";
        public static final String MOVIE_ID = "_id";
        public static final String MOVIE_TITLE = "title";
        public static final String MOVIE_RELEASE_DATE = "date";
        public static final String MOVIE_OVERVIEW = "overview";
        public static final String MOVIE_RATING = "rating";
        public static final String MOVIE_POSTER_PATH = "posterpath";
    }
}
