package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {
    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String TAG = "MainActivity";

    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rvmovies = findViewById(R.id.rvMovies);
        movies = new ArrayList<>();

        // Create an adapter
       final MovieAdapter movieAdapter = new MovieAdapter(this, movies);

        // Set an adapter on the recycler view
        rvmovies.setAdapter(movieAdapter);

        // Set a layout manager on the recycler view
        rvmovies.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonobject = json.jsonObject;
                try {
                    JSONArray results = jsonobject.getJSONArray("results");
                    Log.i(TAG, "Results" + results.toString());
                    movies.addAll(Movie.fromJsdonArray(results));
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Movies" + movies.size());
                } catch (JSONException E) {
                    Log.e(TAG, "Hit json exception", E);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");

            }
        });
    }
}
