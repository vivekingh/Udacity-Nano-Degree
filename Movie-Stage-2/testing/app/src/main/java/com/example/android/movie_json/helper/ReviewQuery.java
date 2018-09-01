package com.example.android.movie_json.helper;

import android.os.AsyncTask;
import android.widget.TextView;

import com.example.android.movie_json.model.Review;
import com.example.android.movie_json.utils.JsonUtils;
import com.example.android.movie_json.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class ReviewQuery extends AsyncTask<URL,Void,ArrayList<Review>> {
    @Override
    protected ArrayList<Review> doInBackground(URL... urls) {
        URL url = urls[0];
        String mResult = "";
        ArrayList<Review> mReview = null;
        try {
            mResult = NetworkUtils.getResponseFromHttpUrl(url);
            mReview = JsonUtils.getReviewList(mResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mReview;
    }

}
