package com.example.android.movie_json;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

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


        /*-------------------------------------------------------------------------------*/
        /*
        mGridView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                mCurrentPage = page;
                displayResult();
                return true;
            }
        });
        */
        mCurrentPage = 1;
        addOnScrollListener();
        /*------------------------------------------------------------------------------*/
        /*
        if (savedInstanceState != null) {
            ArrayList<Parcelable> parcelable = savedInstanceState.
                    getParcelableArrayList(getString(R.string.already_loaded_movie));
            if (parcelable != null) {
                movieList.clear();
                int numMovieObjects = parcelable.size();
                for (int i = 0; i < numMovieObjects; i++) {
                    movieList.add((Movie) parcelable.get(i));
                }
                mMovieAdapter = new MovieAdapter(this,movieList);
                mGridView.setAdapter(mMovieAdapter);
            }
        }
        else {*/
            mMovieAdapter = new MovieAdapter(this, movieList);
            mGridView.setAdapter(mMovieAdapter);
            displayResult();
        //}
    }

    private void addOnScrollListener(){
        mGridView.setOnScrollListener(null);
        mGridView.setOnScrollListener(new EndlessScrollListener(mVisibleThreshold,mCurrentPage) {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                mCurrentPage = page;
                displayResult();
                return true;
            }
        });
    }

    private String getSortMethod(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getString(getString(R.string.sortByKey),getString(R.string.sortBy));
    }

    private void updateSharedPref(String sortMethod){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.sortByKey),sortMethod);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main,menu);
        this.mMenu = menu;
        String sortBy = getSortMethod();
        if(sortBy.equals(getString(R.string.sort_method_popular))){
            mMenu.findItem(R.id.sortby_popularity).setChecked(true);
        }
        else{
            mMenu.findItem(R.id.sortby_highrated).setChecked(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        MenuItem mSortByPopularity = mMenu.findItem(R.id.sortby_popularity);
        MenuItem mSortByHighRated = mMenu.findItem(R.id.sortby_highrated);
        switch(id){
            case R.id.sortby_popularity:
                if(!mSortByPopularity.isChecked() || mMovieAdapter.isEmpty()) {
                    mSortByHighRated.setChecked(false);
                    mSortByPopularity.setChecked(true);
                    updateSharedPref(getString(R.string.sort_method_popular));
                    mMovieAdapter.clear();
                    mMovieAdapter.notifyDataSetChanged();
                    mCurrentPage = 1;
                    addOnScrollListener();
                    displayResult();
                }
                else{
                    Toast.makeText(this,getString(R.string.message_onclick_popular),Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.sortby_highrated:
                if(!mSortByHighRated.isChecked() ||  mMovieAdapter.isEmpty()) {
                    mSortByHighRated.setChecked(true);
                    mSortByPopularity.setChecked(false);
                    updateSharedPref(getString(R.string.sort_method_highrated));
                    mMovieAdapter.clear();
                    mMovieAdapter.notifyDataSetChanged();
                    mCurrentPage=1;
                    addOnScrollListener();
                    displayResult();
                }
                else{
                    Toast.makeText(this,getString(R.string.message_onclick_top_rated),Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void displayResult() {
        URL searchurl = NetworkUtils.buildUrl(getSortMethod(), mCurrentPage);
        new MovieQuery().execute(searchurl);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        int mNoOfMovies = mGridView.getCount();

        ArrayList<Movie> mMovies = new ArrayList<>();
        for (int i = 0; i < mNoOfMovies; i++) {
            mMovies.add((Movie) mGridView.getItemAtPosition(i));
        }

        // Save Movie objects to bundle
        outState.putParcelableArrayList(getString(R.string.already_loaded_movie), mMovies);

        super.onSaveInstanceState(outState);
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
                movieList.clear();
                movieList = JsonUtils.getMovieList(mResult);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return mResult;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            mMovieAdapter.addAll(movieList);
            mMovieAdapter.notifyDataSetChanged();
            pb.setVisibility(View.INVISIBLE);


        }
    }
}
