package com.codepath.apps.mysimpletweets.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.Adapters.TweetsPageAdapter;
import com.codepath.apps.mysimpletweets.Application.TwitterApplication;
import com.codepath.apps.mysimpletweets.Dialogs.ComposeTweetDialog;
import com.codepath.apps.mysimpletweets.Net.TwitterClient;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class TimelineActivity extends AppCompatActivity implements ComposeTweetDialog.ComposeTweetDialogListener {

    private TwitterClient client;
    private User currentUser;
    MenuItem miActionProgressItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (isNetworkAvailable()) {
            client = TwitterApplication.getRestClient();
            client.getCurrentUser(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    currentUser = User.fromJSON(response);
                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putLong("currentUserUid", currentUser.getUid());
                    edit.commit();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("DEBUG", errorResponse.toString());
                }
            }
            );
        }else{
            SharedPreferences pref =
                    PreferenceManager.getDefaultSharedPreferences(this);
            long uid = pref.getLong("currentUserUid", 0);
            currentUser = User.getById(uid);
        }

        //Get de ViewPager and set Adapter
        ViewPager viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setAdapter(new TweetsPageAdapter(getSupportFragmentManager()));
        //Find de Sliding tabStrip and attach the tabStrip to viewpager
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(viewpager);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setLogo(R.drawable.ic_twitter_logo);
        }

    }

    private void profileView() {
        //if (isNetworkAvailable()) {
            Intent i = new Intent(this, ProfileActivity.class);
            i.putExtra("user", currentUser);
            i.putExtra("userId", currentUser.getId());
            startActivity(i);
        //}
    }

    private void composeTweet() {

        if (isNetworkAvailable()) {
            FragmentManager manager = getSupportFragmentManager();
            ComposeTweetDialog dialog = ComposeTweetDialog.newInstance(getString(R.string.fragment_compose_title), currentUser);
            dialog.show(manager, "fragment_compose_tweet");
        }

    }

    public void showProgressBar() {
        // Show progress item
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);
        // Return to finish
        return super.onPrepareOptionsMenu(menu);
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
        } else if (id == R.id.miProfile) {
            profileView();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFinishComposeDialog(String inputText) {
        if (isNetworkAvailable()) {
            if (!inputText.isEmpty()) {
                final String it = inputText;
                client.postTweet(inputText, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Toast.makeText(TimelineActivity.this, it, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Toast.makeText(TimelineActivity.this, "We can not send your tweet", Toast.LENGTH_SHORT).show();
                        Log.d("DEBUG", errorResponse.toString());
                    }
                });
            }else{
                Toast.makeText(TimelineActivity.this, "You can not post an empty Tweet", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean networkConnection = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        if (!networkConnection) {
            Toast.makeText(TimelineActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }
        return networkConnection;
    }

}
