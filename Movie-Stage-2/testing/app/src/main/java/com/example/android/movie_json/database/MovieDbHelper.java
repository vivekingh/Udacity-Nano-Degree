package com.example.android.movie_json.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper{

    private static final String  DATABASE_NAME = "favourite.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_FAVOURITE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry.MOVIE_ID + " INTEGER PRIMARY KEY, " +
                MovieContract.MovieEntry.MOVIE_TITLE + " TEXT, " +
                MovieContract.MovieEntry.MOVIE_RELEASE_DATE + " TEXT, " +
                MovieContract.MovieEntry.MOVIE_OVERVIEW + " TEXT, " +
                MovieContract.MovieEntry.MOVIE_RATING + " TEXT, " +
                MovieContract.MovieEntry.MOVIE_POSTER_PATH + " TEXT " + ")";
        db.execSQL(CREATE_FAVOURITE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
