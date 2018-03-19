package com.example.android.moviebuff.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vvsch on 3/18/2018.
 */

public class Movie implements Parcelable {

    private String mOriginalTitle;
    private String mDescription;
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private String mPosterPath;
    public Double mRating;
    private String mReleaseDate;


    public Movie(){


    }

    public void setmOriginalTitle(String mOriginalTitle) {
        this.mOriginalTitle = mOriginalTitle;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setmPosterPath(String mPosterPath) {
        this.mPosterPath = mPosterPath;
    }

    public void setmRating(Double mRating) {
        this.mRating = mRating;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public String getmOriginalTitle() {
        return mOriginalTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getDetailedRating(){

        return String.valueOf(getmRating()+ "/10");
    }

    public static String getDateFormat() {
        return DATE_FORMAT;
    }

    public String getmPosterPath() {
        final String Poster_URL = "https://image.tmdb.org/t/p/w185";

        return Poster_URL + mPosterPath;
    }

    public Double getmRating() {
        return mRating;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public static Creator<Movie> getCREATOR() {
        return CREATOR;
    }

    protected Movie(Parcel in) {
        mOriginalTitle = in.readString();
        mDescription = in.readString();
        mPosterPath = in.readString();
        if (in.readByte() == 0) {
            mRating = null;
        } else {
            mRating = in.readDouble();
        }
        mReleaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mOriginalTitle);
        parcel.writeString(mDescription);
        parcel.writeString(mPosterPath);
        if (mRating == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(mRating);
        }
        parcel.writeString(mReleaseDate);
    }
}
