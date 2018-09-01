package com.example.android.movie_json.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movie_json.model.Movie;
import com.example.android.movie_json.R;
import com.example.android.movie_json.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends ArrayAdapter<Movie>{

    private Context mContext =null;

    public MovieAdapter( Context context, List<Movie> objects) {
        super(context, 0, objects);
        mContext = context;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Movie movie = getItem(position);

        if(convertView==null){
            convertView  = LayoutInflater.from(getContext()).inflate(
                    R.layout.gridview_populate,parent,false
            );
        }

        ImageView imageView = convertView.findViewById(R.id.movie_poster);
        TextView textView = convertView.findViewById(R.id.texview);

        textView.setText(movie.getTitle());

        Picasso.with(mContext)
                .load(""+NetworkUtils.getImageUrl(movie.getPosterPath()))
                .placeholder(R.drawable.image_placeholder)
                .into(imageView);


        //Log.i("Movieurl",""+NetworkUtils.getImageUrl(movie.poster_path));

        //return super.getView(position, convertView, parent);
        return convertView;
    }
}
