package com.example.android.movie_json;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movie_json.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

public class MovieDetails extends AppCompatActivity {

    private Movie mMovie;
    private TextView mVoteAverage;
    private TextView mOverview;
    private TextView mRelaseDate;
    private TextView mOriginalTitle;
    private ImageView mImageView;

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

        setTitle(mMovie.getTitle());

        addInfoAboutMovie();

    }

    public void addInfoAboutMovie(){

        Picasso.with(this)
                .load(""+NetworkUtils.getImageUrl(mMovie.getPosterPath()))
                .placeholder(R.drawable.image_placeholder)
                .into(mImageView);

        mVoteAverage.setText(mMovie.getVoteAverage());
        mOverview.setText(mMovie.getOverview());
        mRelaseDate.setText(mMovie.getReleasedDate());
        mOriginalTitle.setText(mMovie.getTitle());

    }
}
