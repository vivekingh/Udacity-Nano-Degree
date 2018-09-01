package com.example.android.movie_json.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.movie_json.model.Movie;
import com.example.android.movie_json.database.MovieContract;
import com.example.android.movie_json.database.MovieDbHelper;

import java.util.ArrayList;

public class  FavouriteQuery extends AsyncTask<Void,Void,ArrayList<Movie>>{

    Context mContext;

    public FavouriteQuery(Context context){
        this.mContext = context;
    }

    @Override
    protected ArrayList<Movie> doInBackground(Void... voids) {
        ArrayList<Movie> movies = new ArrayList<>();

        String query = "SELECT * FROM "+ MovieContract.MovieEntry.TABLE_NAME;
        MovieDbHelper mMovieDbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase mDb = mMovieDbHelper.getReadableDatabase();
        Cursor cursor = mDb.rawQuery(query, null);

        while (cursor.moveToNext()){
            movies.add(new Movie(
                    cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_ID)),
                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_TITLE)),
                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_POSTER_PATH)),
                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_OVERVIEW)),
                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_RATING)),
                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_RELEASE_DATE))
            ));
        }
        cursor.close();
        Log.i("LISTSIZE",""+movies.size());
        return movies;
    }
}
