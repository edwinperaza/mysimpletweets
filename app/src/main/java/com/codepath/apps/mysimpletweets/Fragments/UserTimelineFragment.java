package com.codepath.apps.mysimpletweets.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserTimelineFragment extends TweetsListFragment{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static UserTimelineFragment newInstance(User user, long userId) {
        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        args.putLong("userId",userId);
        userTimelineFragment.setArguments(args);
        return userTimelineFragment;
    }

    @Override
    public void populateTimeline() {
        User u = (User) getArguments().getSerializable("user");

        if (!tweets.isEmpty()) {
            oldestTweetUid = tweets.get(tweets.size() - 1).getUid();
        }

        client.getUserTimeline(u.getScreenName(), oldestTweetUid, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                tweets.addAll(Tweet.fromJSONArray(json, false));
                aTweets.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG UserTLFragment:", errorResponse.toString());
            }
        });
    };


    @Override
    public void populateNewTweetsTimeline (){
        User u = (User) getArguments().getSerializable("user");
        long newestTweetUid = 0;
        if (!tweets.isEmpty()) {
            newestTweetUid = tweets.get(0).getUid();
        }
        client.getUserTimelineSince(newestTweetUid, u.getScreenName(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                ArrayList<Tweet> newTweets = Tweet.fromJSONArray(response, false);
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

    @Override
    public void populateTweetsFromDatabase(){
        long userId = getArguments().getLong("userId");
        tweets.addAll(Tweet.getUserTimeline(userId));
        aTweets.notifyDataSetChanged();
    }

}
