package com.example.android.movie_json.activity;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.android.movie_json.R;
import com.example.android.movie_json.adapter.VideoAdapter;
import com.example.android.movie_json.database.MovieContract;
import com.example.android.movie_json.database.MovieDbHelper;
import com.example.android.movie_json.helper.VideoKeyQuery;
import com.example.android.movie_json.model.Movie;
import com.example.android.movie_json.model.Video;
import com.example.android.movie_json.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieDetails extends AppCompatActivity {

    private Movie mMovie;
    private TextView mVoteAverage;
    private TextView mOverview;
    private TextView mRelaseDate;
    private TextView mOriginalTitle;
    private ImageView mImageView;

    private ToggleButton mToggleButton;
    private MovieDbHelper mMovieDbHelper;
    private SQLiteDatabase mDb;

    private RecyclerView mVideoRecyclerView;
    private VideoAdapter mVideoAdapter;
    private ArrayList<Video> mVideo;

    private TextView mTextError;
    private TextView mReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        Intent intent = getIntent();
        mMovie = intent.getParcelableExtra(getString(R.string.transferred_movie_detail));
        mVoteAverage = findViewById(R.id.vote_average);
        mOverview = findViewById(R.id.about);
        mRelaseDate = findViewById(R.id.release_date);
        mOriginalTitle = findViewById(R.id.original_title);
        mImageView = findViewById(R.id.detailimageview);

        mToggleButton = findViewById(R.id.toggle_button);
        mMovieDbHelper = new MovieDbHelper(this);
        mDb = mMovieDbHelper.getWritableDatabase();

        mVideo = new ArrayList<>();
        mTextError = findViewById(R.id.error_recyclerview);

        setTitle(mMovie.getTitle());
        addInfoAboutMovie();
        updateIfFavourite();
        addOnClickListeners();

        videoSetup();

        mReview = findViewById(R.id.review_click);
        mReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startReviewActivity();
            }
        });

    }

    void startReviewActivity(){
        Intent intent = new Intent(this, ReviewActivity.class);
        intent.putExtra(getString(R.string.transferred_movie_detail), mMovie);
        startActivity(intent);
    }

    private void videoSetup(){
        mVideoRecyclerView = findViewById(R.id.video_recycleview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mVideoRecyclerView.setLayoutManager(linearLayoutManager);
        mVideoRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mVideoRecyclerView.addItemDecoration(new SpacingItemDecoration(5));

        mVideoAdapter = new VideoAdapter(getApplicationContext(), mVideo, new VideoAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(Video clickedVideo) {
                watchYoutubeVideo(clickedVideo.getKey());
            }
        });
        mVideoRecyclerView.setAdapter(mVideoAdapter);

        new VideoKeyQuery(){
            @Override
            protected void onPostExecute(ArrayList<Video> videos) {
                super.onPostExecute(videos);
                if(videos!=null && videos.size()>0) {
                    mVideo.addAll(videos);
                    mVideoAdapter.notifyDataSetChanged();
                    mTextError.setVisibility(View.GONE);
                }
                else if(!isNetworkAvailable()){
                    mTextError.setText("Check your internet");
                    mTextError.setVisibility(View.VISIBLE);
                    //no internet
                }
                else{
                    mTextError.setText("Clips are unavailable.");
                    mTextError.setVisibility(View.VISIBLE);
                }
            }
        }.execute(NetworkUtils.getVideoUrl(mMovie.getId()));
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void addInfoAboutMovie(){

        Picasso.with(this)
                .load(""+NetworkUtils.getImageUrl(mMovie.getPosterPath()))
                .placeholder(R.drawable.image_placeholder)
                .into(mImageView);

        mVoteAverage.setText(mMovie.getVoteAverage());
        mOverview.setText(mMovie.getOverview());
        mRelaseDate.setText(mMovie.getReleasedDate());
        mOriginalTitle.setText(mMovie.getTitle());

    }

    private void updateIfFavourite(){
        String Query = "Select * from " + MovieContract.MovieEntry.TABLE_NAME + " where " +
                MovieContract.MovieEntry.MOVIE_ID + " = " + mMovie.getId();
        Cursor cursor = mDb.rawQuery(Query, null);
        if(cursor.getCount()>0){
            mToggleButton.setChecked(true);
        }
        else{
            mToggleButton.setChecked(false);
        }
        cursor.close();
    }

    private void addOnClickListeners(){
        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    addFavouriteMovie();
                    Toast.makeText(getApplicationContext(),getString(R.string.message_on_adding_favourite_movie),Toast.LENGTH_SHORT).show();
                } else {
                    boolean mRemoved = deleteFromFavourite();
                    if(mRemoved) {
                        //MainActivity.mMovieAdapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), getString(R.string.message_on_remove_favourite_movie), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void addFavouriteMovie(){
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.MOVIE_ID,mMovie.getId());
        cv.put(MovieContract.MovieEntry.MOVIE_TITLE,mMovie.getTitle());
        cv.put(MovieContract.MovieEntry.MOVIE_RATING,mMovie.getVoteAverage());
        cv.put(MovieContract.MovieEntry.MOVIE_RELEASE_DATE,mMovie.getReleasedDate());
        cv.put(MovieContract.MovieEntry.MOVIE_OVERVIEW,mMovie.getOverview());
        cv.put(MovieContract.MovieEntry.MOVIE_POSTER_PATH,mMovie.getPosterPath());
        mDb.insert(MovieContract.MovieEntry.TABLE_NAME,null,cv);
    }

    private boolean deleteFromFavourite(){
        return mDb.delete(MovieContract.MovieEntry.TABLE_NAME,
                MovieContract.MovieEntry.MOVIE_ID + "="+ mMovie.getId(),null)>0;
    }



    public void watchYoutubeVideo(String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }


    private class SpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spacing;

        public SpacingItemDecoration(int spacing) {
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            if (position == 0) {
                return;
            }
            outRect.left = spacing;
        }
    }

}
