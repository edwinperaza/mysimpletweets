package com.codepath.apps.mysimpletweets.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeTimelineFragment extends TweetsListFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void populateTimeline (){

        if (!tweets.isEmpty()) {
            oldestTweetUid = tweets.get(tweets.size() - 1).getUid();
        }

        client.getHomeTimelineBefore(oldestTweetUid, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                tweets.addAll(Tweet.fromJSONArray(json,false));
                aTweets.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("HOMETL POPULATE ERROR: ", errorResponse.toString());
            }
        });
    };

    @Override
    public void populateTweetsFromDatabase(){
        //tweets.clear();
        tweets.addAll(Tweet.getAllTimeline());
        aTweets.notifyDataSetChanged();
    }

    @Override
    public void populateNewTweetsTimeline (){
        long newestTweetUid = 0;
        if (!tweets.isEmpty()) {
            newestTweetUid = tweets.get(0).getUid();
        }
        client.getHomeTimelineSince(newestTweetUid, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                ArrayList<Tweet> newTweets = Tweet.fromJSONArray(response,false);
                if (!newTweets.isEmpty() && newTweets.size()>0) {
                    for (int i = 0; i < newTweets.size(); i++) {
                        aTweets.insert(newTweets.get(i), 0);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("DEBUG NEW TWEETS", errorResponse.toString());
            }
        });
    }



}
