package com.example.android.moviebuff;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.android.moviebuff.Model.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by vvsch on 3/18/2018.
 */

public class customAdapter extends BaseAdapter  {

    private final Context mContext;

    private final Movie[] mMovie;


    public customAdapter(Context context, Movie[] movies){

        mContext = context;
        mMovie = movies;
    }

    @Override
    public int getCount() {

        if (mMovie == null || mMovie.length == 0) return -1;

        return mMovie.length;
    }

    @Override
    public Object getItem(int position) {

        if (mMovie == null || mMovie.length == 0)return -1;

        return mMovie[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ImageView imageView;
        if (view == null){
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
        } else imageView = (ImageView)view;

        Picasso.with(mContext)
                .load(mMovie[i].getmPosterPath())
                .resize(185,278)
                .error(null)
                .placeholder(null)
                .into(imageView);


        return imageView;
    }


}
