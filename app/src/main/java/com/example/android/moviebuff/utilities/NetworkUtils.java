
package com.example.android.moviebuff.utilities;

import android.content.Context;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v4.app.ShareCompat;
import android.util.Log;
import android.net.Uri;

import com.example.android.moviebuff.MainActivity;
import com.example.android.moviebuff.Model.Movie;
import com.example.android.moviebuff.MovieUpdateCallback;
import com.example.android.moviebuff.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public final class NetworkUtils extends AsyncTask<String,Void,Movie[]> {

    private final String LOG_TAG = NetworkUtils.class.getSimpleName();
    //private static final String API_key = "4b8b106215e25159f7dc631309774958";
    private final String mApiKey;
    //private static final String url1 = "http://api.themoviedb.org/3/movie/popular?api_key="+ API_key;

    private final MovieUpdateCallback mListener;


    public NetworkUtils(MovieUpdateCallback listener, String apiKey) {

        super();
        mApiKey = apiKey;
        mListener = listener;
    }


    @Override
    protected Movie[] doInBackground(String... strings) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieJSON = null;

        try {
            URL url = getApiUrl(strings);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder builder = new StringBuilder();

            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {

                builder.append(line).append("\n");
            }
            if (builder.length() == 0) {
                return null;
            }
            movieJSON = builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getSimpleStringsFromJson(movieJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new Movie[0];
    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        super.onPostExecute(movies);

        // Notify UI
        mListener.processingImages(movies);
    }

    public static Movie[] getSimpleStringsFromJson( String MovieJson ) throws JSONException {

        final String result = "results";
        final String originalTitle = "original_title";
        final String description = "overview";
        final String posterPath = "poster_path";
        final String rating ="vote_average" ;
        final String releaseDate = "release_date";

        JSONObject movieJSON = new JSONObject(MovieJson);
        JSONArray resultsArray = movieJSON.getJSONArray(result);

        Movie [] movieParsedData = new Movie[resultsArray.length()]; // Array of movie that stores data from JSON string

        for (int i = 0 ; i < resultsArray.length();i++){

            movieParsedData [i]= new Movie();

            JSONObject movieinfo = resultsArray.getJSONObject(i);

            movieParsedData[i].setmOriginalTitle(movieinfo.getString(originalTitle));
            movieParsedData[i].setmPosterPath(movieinfo.getString(description));
            movieParsedData[i].setmPosterPath(movieinfo.getString(posterPath));
            movieParsedData[i].setmRating(movieinfo.getDouble(rating));

        }


        return null;
    }

    private URL getApiUrl(String[] parameters) throws MalformedURLException {

        final String BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
        final String SORT_BY_PARAM = "sort_by";
        final String API_KEY_PARAM = "api_key";

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(SORT_BY_PARAM, parameters[0])
                .appendQueryParameter(API_KEY_PARAM, mApiKey)
                .build();

        return new URL(builtUri.toString());

    }



}

