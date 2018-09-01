package com.example.android.movie_json.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.movie_json.R;
import com.example.android.movie_json.adapter.MovieAdapter;
import com.example.android.movie_json.helper.EndlessScrollListener;
import com.example.android.movie_json.helper.FavouriteQuery;
import com.example.android.movie_json.model.Movie;
import com.example.android.movie_json.utils.JsonUtils;
import com.example.android.movie_json.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ProgressBar pb;
    private ArrayList<Movie> movieList;
    private GridView mGridView;
    private MovieAdapter mMovieAdapter;
    private int mCurrentPage = 1;
    private Menu mMenu;
    private int mVisibleThreshold = 5;
    private MenuItem mSortByPopularity;
    private MenuItem mSortByHighRated;
    private MenuItem mSortByFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pb = findViewById(R.id.progress_bar);

        mGridView = findViewById(R.id.gridview);
        movieList = new ArrayList<>();


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), MovieDetails.class);
                intent.putExtra(getString(R.string.transferred_movie_detail), movie);
                startActivity(intent);
            }
        });

        mCurrentPage = getPageNo();


        if (!getSortMethod().equals(getString(R.string.sort_method_favourite))) {
            addOnScrollListener();
        }
        else{
            removeOnScrollListener();
        }

        if(savedInstanceState!=null){
            ArrayList<Parcelable> parcelable = savedInstanceState.
                    getParcelableArrayList(getString(R.string.already_loaded_movie));
            if (parcelable != null) {
                //movieList.clear();
                int numMovieObjects = parcelable.size();
                for (int i = 0; i < numMovieObjects; i++) {
                    movieList.add((Movie) parcelable.get(i));
                }
            }
            Log.i("ArraySize : "," "+movieList.size()+"  "+getPageNo());
        }

        mMovieAdapter = new MovieAdapter(this, movieList);
        mGridView.setAdapter(mMovieAdapter);

        if(savedInstanceState==null) {
            mCurrentPage = 1;
            updatePageNo();
            displayResult();
        }
    }

    private void addOnScrollListener() {
        mGridView.setOnScrollListener(null);
        mGridView.setOnScrollListener(new EndlessScrollListener(mVisibleThreshold, mCurrentPage) {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                mCurrentPage = page;
                updatePageNo();
                displayResult();
                return true;
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        updatePageNo();
    }

    private void removeOnScrollListener() {
        mGridView.setOnScrollListener(null);
    }

    private String getSortMethod() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getString(getString(R.string.sortByKey), getString(R.string.sortBy));
    }

    private int getPageNo(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getInt(getString(R.string.pageNoKey),
                getApplicationContext().getResources().getInteger(R.integer.currentPageNo));
    }

    private void updatePageNo(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getString(R.string.pageNoKey),mCurrentPage);
        editor.apply();
    }

    private void updateSharedPref(String sortMethod) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.sortByKey), sortMethod);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        this.mMenu = menu;

        mSortByPopularity = mMenu.findItem(R.id.sortby_popularity);
        mSortByHighRated = mMenu.findItem(R.id.sortby_highrated);
        mSortByFavourite = mMenu.findItem(R.id.sortby_favourite);

        String sortBy = getSortMethod();

        if (sortBy.equals(getString(R.string.sort_method_popular))) {
            mSortByPopularity.setChecked(true);
        } else if (sortBy.equals(getString(R.string.sort_method_highrated))) {
            mSortByHighRated.setChecked(true);
        } else {
            mSortByFavourite.setChecked(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.sortby_popularity:
                handlePopularSelect();
                return true;

            case R.id.sortby_highrated:
                handleHighRatedSelect();
                return true;

            case R.id.sortby_favourite:
                handleFavouriteSelect();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void handleHighRatedSelect() {
        if (!mSortByHighRated.isChecked() || mMovieAdapter.isEmpty()) {
            mSortByPopularity.setChecked(false);
            mSortByFavourite.setChecked(false);
            mSortByHighRated.setChecked(true);
            updateSharedPref(getString(R.string.sort_method_highrated));
            movieList.clear();
            mMovieAdapter.notifyDataSetChanged();
            mCurrentPage = 1;
            updatePageNo();
            addOnScrollListener();
            displayResult();
        } else {
            Toast.makeText(this, getString(R.string.message_onclick_top_rated), Toast.LENGTH_SHORT).show();
        }
    }

    private void handlePopularSelect() {
        if (!mSortByPopularity.isChecked() || mMovieAdapter.isEmpty()) {
            mSortByHighRated.setChecked(false);
            mSortByFavourite.setChecked(false);
            mSortByPopularity.setChecked(true);
            updateSharedPref(getString(R.string.sort_method_popular));
            movieList.clear();
            mMovieAdapter.notifyDataSetChanged();
            mCurrentPage = 1;
            updatePageNo();
            addOnScrollListener();
            displayResult();
        } else {
            Toast.makeText(this, getString(R.string.message_onclick_popular), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleFavouriteSelect() {
        if (!mSortByFavourite.isChecked() || mMovieAdapter.isEmpty()) {
            mSortByHighRated.setChecked(false);
            mSortByPopularity.setChecked(false);
            mSortByFavourite.setChecked(true);
            updateSharedPref(getString(R.string.sort_method_favourite));
            movieList.clear();
            mMovieAdapter.notifyDataSetChanged();
            mCurrentPage = 1;
            updatePageNo();
            removeOnScrollListener();
            displayResult();
        } else {
            Toast.makeText(this, getString(R.string.message_onclick_favourite), Toast.LENGTH_SHORT).show();
        }
    }

    private void displayResult() {
        if (!getSortMethod().equals(getString(R.string.sort_method_favourite))) {
            URL searchurl = NetworkUtils.buildUrl(getSortMethod(), mCurrentPage);
            new MovieQuery().execute(searchurl);
        } else {
            Log.i("MYMESSAGE","FavouriteQuery is called.");
            new FavouriteQuery(this){
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    pb.setVisibility(View.VISIBLE);
                }

                @Override
                protected void onPostExecute(ArrayList<Movie> movies) {
                    super.onPostExecute(movies);
                    movieList.clear();
                    movieList.addAll(movies);
                    mMovieAdapter.notifyDataSetChanged();
                    pb.setVisibility(View.INVISIBLE);
                }
            }.execute();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getSortMethod().equals(getString(R.string.sort_method_favourite))){
            displayResult();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        int mNoOfMovies = mGridView.getCount();

        ArrayList<Movie> mMovies = new ArrayList<>();
        for (int i = 0; i < mNoOfMovies; i++) {
            mMovies.add((Movie) mGridView.getItemAtPosition(i));
        }

        // Save Movie objects to bundle
        outState.putParcelableArrayList(getString(R.string.already_loaded_movie), mMovies);


    }

    public class MovieQuery extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String mResult = "";
            try {
                mResult = NetworkUtils.getResponseFromHttpUrl(url);
                movieList.addAll(JsonUtils.getMovieList(mResult));

            } catch (IOException e) {
                e.printStackTrace();
            }

            return mResult;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mMovieAdapter.notifyDataSetChanged();
            pb.setVisibility(View.INVISIBLE);
        }
    }
}
