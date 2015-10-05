package com.codepath.apps.mysimpletweets.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.Dialogs.ComposeTweetDialog;
import com.codepath.apps.mysimpletweets.Helpers.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.Adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.Application.TwitterApplication;
import com.codepath.apps.mysimpletweets.Net.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends AppCompatActivity implements ComposeTweetDialog.ComposeTweetDialogListener {

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private ListView lvTweets;
    private long lowestTweetUid = Long.MAX_VALUE;
    private User currentUser;
    private JsonHttpResponseHandler moreTweetsHandler;
    private SwipeRefreshLayout swipeContainer;


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

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNewerTweets();
            }
        });


        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemCount) {
                getOlderTweets();
                return true;
            }
        });

        client.getCurrentUser(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                currentUser = User.fromJson(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        getOlderTweets();
    }

    private void getNewerTweets() {
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
                        aTweets.insert(newTweets.get(i),0);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("DEBUG", errorResponse.toString());
            }
        });

        swipeContainer.setRefreshing(false);
    }

    private void getOlderTweets() {
        long oldestTweetUid = Long.MAX_VALUE;
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
                Log.d("DEBUG", errorResponse.toString());
            }
        });
        swipeContainer.setRefreshing(false);
    }


    private void composeTweet() {
        FragmentManager manager = getSupportFragmentManager();
        ComposeTweetDialog dialog = ComposeTweetDialog.newInstance(getString(R.string.fragment_compose_title), currentUser);
        dialog.show(manager, "fragment_compose_tweet");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.tweet_compose) {
                composeTweet();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFinishComposeDialog(String inputText) {
        Toast.makeText(this, inputText, Toast.LENGTH_SHORT).show();
        client.postTweet(inputText, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                 getNewerTweets();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }


}
