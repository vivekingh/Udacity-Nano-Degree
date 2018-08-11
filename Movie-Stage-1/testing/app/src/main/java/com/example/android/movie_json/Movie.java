package com.example.android.movie_json;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable{
    private String title;
    private String poster_path;
    private String overview;
    private String vote_average;
    private String released_date;
    private int id;
    public Movie(int mId, String mTitle,
                 String mPosterPath, String mOverview,
                 String mVoteAverage, String mReleaseDate){

        this.id = mId;
        this.title = mTitle;
        this.poster_path = mPosterPath;
        this.overview = mOverview;
        this.vote_average = mVoteAverage;
        this.released_date = mReleaseDate;

    }

    protected Movie(Parcel in) {

        this.title = in.readString();
        this.poster_path = in.readString();
        this.overview = in.readString();
        this.vote_average = in.readString();
        this.released_date = in.readString();
        this.id = in.readInt();

    }



    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getVoteAverage() {
        return vote_average;
    }

    public String getReleasedDate() {
        return released_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(poster_path);
        dest.writeString(overview);
        dest.writeString(vote_average);
        dest.writeString(released_date);
        dest.writeInt(id);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
