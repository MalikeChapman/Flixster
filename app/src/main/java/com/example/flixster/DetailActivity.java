package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class DetailActivity extends YouTubeBaseActivity {

    private static final String YOUTUBE_API_KEY = "AIzaSyC1qS9fMGyZi5NB7gSEaE2fdswirrluem0";
    private  static final String VIDEOS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    TextView tvTitle;
    TextView tvOverview;
    RatingBar ratingbar;
    YouTubePlayerView mYouTubePlayerView;
    Movie reference;
    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        ratingbar = findViewById(R.id.movieRatingBar);
        mYouTubePlayerView = findViewById(R.id.player);


        Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        reference = movie;
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        ratingbar.setRating((float) movie.getRating());




        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(VIDEOS_URL, movie.getMovieID()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                try{
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    if (results.length() == 0){
                        return;
                    }
                    String youtubeKey = results.getJSONObject(0).getString("key");
                    Log.d("DetailActivity", youtubeKey);
                    initializeYoutube(youtubeKey);

                } catch (JSONException e){
                    Log.e("DetailActivity", "Failed to parse JSON", e);

                }

            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {

            }
        });


    }

    private void initializeYoutube(final String youtubeKey) {
        mYouTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d("DetailActivity", "onInitializationSuccess");
                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    youTubePlayer.setFullscreen(true);

                    if (reference.getRating() <= 5){
                        youTubePlayer.cueVideo(youtubeKey);


                    }
                    else {
                        youTubePlayer.loadVideo(youtubeKey);
                    }

                } else {

                }

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("DetailActivity", "onInitializationFailure");

            }
        });
    }


}
