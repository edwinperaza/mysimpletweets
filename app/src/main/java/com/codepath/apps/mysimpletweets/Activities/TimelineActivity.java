package com.codepath.apps.mysimpletweets.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.Dialogs.ComposeTweetDialog;
import com.codepath.apps.mysimpletweets.Fragments.TweetsListFragment;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends AppCompatActivity implements ComposeTweetDialog.ComposeTweetDialogListener {

//    private TwitterClient client;
    private SwipeRefreshLayout swipeContainer;


    private TweetsListFragment fragmentTweetsList;

    private long lowestTweetUid = Long.MAX_VALUE;
    private User currentUser;
    private JsonHttpResponseHandler moreTweetsHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
//        client.postTweet(inputText, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                 getNewerTweets();
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                Log.d("DEBUG", errorResponse.toString());
//            }
//        });
    }


}
