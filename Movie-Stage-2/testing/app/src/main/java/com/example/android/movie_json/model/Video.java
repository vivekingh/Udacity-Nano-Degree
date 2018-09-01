package com.example.android.movie_json.model;

public class Video {
    private String key;
    private String name;
    public Video(String mkey){
        this.key = mkey;
    }
    public Video(String mKey,String mName){
        this.key = mKey;
        this.name = mName;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}
