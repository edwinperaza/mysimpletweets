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

    public static UserTimelineFragment newInstance(User user) {
        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        userTimelineFragment.setArguments(args);
        return userTimelineFragment;
    }

    @Override
    public void populateTimeline(long sinceId, long maxId) {
        User u = (User) getArguments().getSerializable("user");

        client.getUserTimeline(u.getScreenName(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                tweets.addAll(Tweet.fromJSONArray(json));
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
