package com.example.android.movie_json.activity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.android.movie_json.R;
import com.example.android.movie_json.adapter.ReviewAdapter;
import com.example.android.movie_json.helper.ReviewQuery;
import com.example.android.movie_json.model.Movie;
import com.example.android.movie_json.model.Review;
import com.example.android.movie_json.utils.NetworkUtils;

import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {

    private Movie mMovie;
    private RecyclerView mReviewRecyclerView;
    private ReviewAdapter mReviewAdapter;
    private ArrayList<Review> mReview;
    private TextView mReviewError;
    private ProgressBar mReviewProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);



        mReviewError = findViewById(R.id.review_error);
        mReviewProgress = findViewById(R.id.review_progressbar);

        Intent intent = getIntent();
        mMovie = intent.getParcelableExtra(getString(R.string.transferred_movie_detail));

        setTitle("Reviews of "+mMovie.getTitle());

        mReviewRecyclerView = findViewById(R.id.review_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mReviewRecyclerView.setLayoutManager(linearLayoutManager);
        mReview = new ArrayList<>();
        mReviewAdapter = new ReviewAdapter(this,mReview);
        mReviewRecyclerView.setAdapter(mReviewAdapter);

        new ReviewQuery(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mReviewProgress.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(ArrayList<Review> reviews) {
                super.onPostExecute(reviews);
                if(reviews!=null && reviews.size()>0){
                    mReview.addAll(reviews);
                    mReviewAdapter.notifyDataSetChanged();
                    mReviewError.setVisibility(View.GONE);
                }
                else if(!isNetworkAvailable()){
                    mReviewError.setText("Check your internet!");
                    mReviewError.setVisibility(View.VISIBLE);
                }
                else{
                    mReviewError.setText("Reviews are unavailable!");
                    mReviewError.setVisibility(View.VISIBLE);
                }
                mReviewProgress.setVisibility(View.GONE);
            }
        }.execute(NetworkUtils.getReviewUrl(mMovie.getId()));

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
