package com.example.android.movie_json.model;

public class Review {
    private String author;
    private String content;
    public Review(String mAuthor, String mContent){
        this.author = mAuthor;
        this.content = mContent;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
