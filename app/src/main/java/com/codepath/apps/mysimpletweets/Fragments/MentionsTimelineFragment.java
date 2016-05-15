package com.codepath.apps.mysimpletweets.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public class MentionsTimelineFragment extends TweetsListFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void populateTimeline (){

        if (!tweets.isEmpty()) {
            oldestTweetUid = tweets.get(tweets.size() - 1).getUid();
        }

        client.getMentionsTimeline(oldestTweetUid, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                tweets.addAll(Tweet.fromJSONArray(json, true));
                aTweets.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("MENT_TL POPULATE ERROR:", errorResponse.toString());
            }
        });
    };

    @Override
    public void populateTweetsFromDatabase(){
//        tweets.clear();
        tweets.addAll(Tweet.getAllMentions());
        aTweets.notifyDataSetChanged();
    }

}
