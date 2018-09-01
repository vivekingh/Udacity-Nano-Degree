package com.example.android.movie_json.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.movie_json.R;
import com.example.android.movie_json.model.Review;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{

    private ArrayList<Review> mReview;
    private Context mContext;

    public ReviewAdapter(Context context,ArrayList<Review> reviews){
        this.mReview = reviews;
        this.mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTextAuthor;
        TextView mContent;
        public ViewHolder(View itemView) {
            super(itemView);
            mTextAuthor = itemView.findViewById(R.id.text_view_review_author);
            mContent = itemView.findViewById(R.id.text_view_review_content);
        }
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.review_populate,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        Review review = mReview.get(position);
        holder.mTextAuthor.setText(review.getAuthor());
        holder.mContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mReview.size();
    }
}
