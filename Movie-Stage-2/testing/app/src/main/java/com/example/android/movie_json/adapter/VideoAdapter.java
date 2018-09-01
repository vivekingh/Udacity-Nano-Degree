package com.example.android.movie_json.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movie_json.R;
import com.example.android.movie_json.model.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private ArrayList<Video> mVideo;
    private Context mContext;
    private String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg";
    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener{
        void onListItemClick(Video clickedVideo);
    }

    public VideoAdapter(Context context,ArrayList<Video> videos,ListItemClickListener listener){
        this.mVideo = videos;
        this.mContext = context;
        this.mOnClickListener = listener;
        //this.mOnClickListener =  (ListItemClickListener) context.getApplicationContext();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView mVideoThumbnail;
        TextView mVideoTextView;
        public VideoViewHolder(View itemView) {
            super(itemView);
            mVideoThumbnail = itemView.findViewById(R.id.video_imageview);
            mVideoTextView = itemView.findViewById(R.id.video_textview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(mVideo.get(clickedPosition));
        }
    }

    @NonNull
    @Override
    public VideoAdapter.VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_populate,parent,false);
        return new VideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.VideoViewHolder holder, int position) {
        Video video = mVideo.get(position);
        holder.mVideoTextView.setText(video.getName());
        Picasso.with(mContext)
                .load(String.format(YOUTUBE_THUMBNAIL_URL, video.getKey()))
                .placeholder(R.drawable.image_placeholder)
                .into(holder.mVideoThumbnail);
    }

    @Override
    public int getItemCount() {
        return mVideo.size();
    }
}
