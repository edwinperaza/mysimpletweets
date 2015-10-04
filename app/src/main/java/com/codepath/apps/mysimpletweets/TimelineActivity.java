package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.ArrayList;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private ListView lvTweets;
    private long lowestTweetUid = Long.MAX_VALUE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lvTweets = (ListView) findViewById(R.id.lvTweets);

        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(this, tweets);
        lvTweets.setAdapter(aTweets);

        client = TwitterApplication.getRestClient();

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
                        @Override
                        public boolean onLoadMore(int page, int totalItemCount) {
                            populateTimeLine(lowestTweetUid);
                            return true;
                            }
                    });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        populateTimeLine(lowestTweetUid);
    }

    private void populateTimeLine(long lowestValue){
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);
                aTweets.addAll(tweets);

                int size = tweets.size();
                long lastUid = tweets.get(size-1).getUid();

                if (lastUid < lowestTweetUid) {
                    lowestTweetUid = lastUid;
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        };

        if (lowestValue == Long.MAX_VALUE) {
            client.getHomeTimeLineFirst(handler);
        } else {
            client.getHomeTimeline(handler, lowestValue - 1);
        }
    }

}
