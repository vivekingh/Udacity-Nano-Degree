package com.example.android.movie_json.helper;

import android.os.AsyncTask;

import com.example.android.movie_json.model.Video;
import com.example.android.movie_json.utils.JsonUtils;
import com.example.android.movie_json.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class VideoKeyQuery extends AsyncTask<URL,Void,ArrayList<Video>>{
    @Override
    protected ArrayList<Video> doInBackground(URL... urls) {
        URL url = urls[0];
        String mResult = "";
        ArrayList<Video> mVideo = null;
        try {
            mResult = NetworkUtils.getResponseFromHttpUrl(url);
            mVideo = JsonUtils.getVideoKeyList(mResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mVideo;
    }
}
