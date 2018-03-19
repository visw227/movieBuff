package com.example.android.moviebuff;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.moviebuff.Model.Movie;
import com.example.android.moviebuff.utilities.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    GridView mGridView;
    private final String TAG = MainActivity.class.getSimpleName();
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView = (GridView) findViewById(R.id.grid);

        //pd = new ProgressDialog(getApplicationContext());


       // mGridView.setOnItemClickListener(moviePosterClickListener);
        if (savedInstanceState == null) {
            // Get data from the Internet
            getMovies(getSortMethod());
        } else {
            // Get data from local resources
            // Get Movie objects
            Parcelable[] parcelable = savedInstanceState.
                    getParcelableArray(getString(R.string.parcel_movie));

            if (parcelable != null) {
                int numMovieObjects = parcelable.length;
                Movie[] movies = new Movie[numMovieObjects];
                for (int i = 0; i < numMovieObjects; i++) {
                    movies[i] = (Movie) parcelable[i];
                }

                // Load movie objects into view
                mGridView.setAdapter(new customAdapter(this, movies));
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Log.v(LOG_TAG, "onSaveInstanceState");

        int numMovieObjects = mGridView.getCount();
        if (numMovieObjects > 0) {
            // Get Movie objects from gridview
            Movie[] movies = new Movie[numMovieObjects];
            for (int i = 0; i < numMovieObjects; i++) {
                movies[i] = (Movie) mGridView.getItemAtPosition(i);
            }

            // Save Movie objects to bundle
            outState.putParcelableArray(getString(R.string.parcel_movie), movies);
        }

        super.onSaveInstanceState(outState);
    }


      //  mGridView.setOnItemClickListener(moviePosterClickListener);







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,mMenu);
        mMenu = menu;
        mMenu.add(Menu.NONE,R.string.pref_sort_pop_desc_key,Menu.NONE,null)
                .setVisible(false)
                .setTitle("Popularity")
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        mMenu.add(Menu.NONE,R.string.pref_sort_vote_avg_desc_key,Menu.NONE,null)
                .setVisible(false)
                .setTitle("Rating")
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.string.pref_sort_pop_desc_key :
                updateSharedPrefs(getString(R.string.tmdb_sort_pop_desc));
                updateMenu();
                getMovies(getSortMethod());
                return true;
            case R.string.pref_sort_vote_avg_desc_key:
                updateSharedPrefs(getString(R.string.tmdb_sort_vote_avg_desc));
                updateMenu();
                getMovies(getSortMethod());
                return true;

            default:

        }

        return super.onOptionsItemSelected(item);
    }

    private void updateMenu(){
        String sortMethod = getSortMethod();
        if (sortMethod.equals(getString(R.string.tmdb_sort_pop_desc))){
            mMenu.findItem(R.string.pref_sort_pop_desc_key).setVisible(false);
            mMenu.findItem(R.string.pref_sort_vote_avg_desc_key).setVisible(true);
        }else {
            mMenu.findItem(R.string.pref_sort_vote_avg_desc_key).setVisible(false);
            mMenu.findItem(R.string.pref_sort_pop_desc_key).setVisible(true);
        }
    }

    private void getMovies (String sortMethod){
        if(isNetworkAvailable()){
            String apiKey = "4b8b106215e25159f7dc631309774958";

            MovieUpdateCallback task = new MovieUpdateCallback() {
                @Override
                public void processingImages(Movie[] results) {
                    mGridView.setAdapter(new customAdapter(getApplicationContext(),results));
                }
            };

            NetworkUtils movieTask = new NetworkUtils(task,apiKey);
            movieTask.execute(sortMethod);
        } else {
            Toast.makeText(this, getString(R.string.error_need_internet), Toast.LENGTH_LONG).show();

        }
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private String getSortMethod(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(getString(R.string.pref_sort_method_key),getString(R.string.pref_sort_pop_desc_key));
    }
    private void updateSharedPrefs(String sortMethod){
        SharedPreferences sharedpref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedpref.edit();
        editor.putString(getString(R.string.pref_sort_method_key),sortMethod);
        editor.apply();
    }
}

