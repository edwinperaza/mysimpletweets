package com.codepath.apps.mysimpletweets.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.codepath.apps.mysimpletweets.Activities.ProfileActivity;
import com.codepath.apps.mysimpletweets.Adapters.TweetsArrayAdapter;
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

        aTweets.setImageProfileClickListener(new TweetsArrayAdapter.OnImageProfileClickListener() {
            @Override
            public void onImageProfileClick(View itemView, int position) {
                Tweet tweet = aTweets.getItem(position);
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra("screen_name", tweet.getUser().getScreenName());
                intent.putExtra("user", tweet.getUser());
                startActivity(intent);
            }
        });

    }

    @Override
    public void populateTimeline (long sinceId, long maxId){
        if (!tweets.isEmpty()) {
            oldestTweetUid = tweets.get(tweets.size() - 1).getUid();
        }
        client.getHomeTimelineBefore(oldestTweetUid, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                ArrayList<Tweet> newTweets = Tweet.fromJSONArray(response);
                tweets.addAll(newTweets);
                aTweets.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG POPULATE TIMELINE", errorResponse.toString());
            }
        });
    };

    @Override
    public void populateNewTweetsTimeline (){
        long newestTweetUid = 0;
        if (!tweets.isEmpty()) {
            newestTweetUid = tweets.get(0).getUid();
        }
        client.getHomeTimelineSince(newestTweetUid, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                ArrayList<Tweet> newTweets = Tweet.fromJSONArray(response);
                if (!newTweets.isEmpty()) {
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
