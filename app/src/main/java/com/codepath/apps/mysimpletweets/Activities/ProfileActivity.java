package com.codepath.apps.mysimpletweets.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.Application.TwitterApplication;
import com.codepath.apps.mysimpletweets.Fragments.UserTimelineFragment;
import com.codepath.apps.mysimpletweets.Net.TwitterClient;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    TwitterClient client;
    User user;
    TextView tvFollowers;
    TextView tvFollowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = (User) getIntent().getSerializableExtra("user");
        if (user == null){

            client = TwitterApplication.getRestClient();
            client.getCurrentUser(new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.saveCurrentUser(response,true);
                    getSupportActionBar().setTitle("@" + user.getScreenName());
                }
            });
        }

        populateProfileHeader(user);

        if (savedInstanceState == null) {
            //Create de user timeline fragment
            UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(user);
            // Display user timeline Fragment within this activity (dynamically)
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, userTimelineFragment);
            ft.commit();
        }

        tvFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FollowActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("type", "following");
                startActivity(intent);
            }
        });

        tvFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FollowActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("type", "followers");
                startActivity(intent);
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setLogo(R.drawable.ic_twitter_logo);
        }

    }

    private void populateProfileHeader(User u){

        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        ImageView ivVerified = (ImageView) findViewById(R.id.ivVerified);
        tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

        tvName.setText(u.getName());
        tvTagline.setText(u.getTagline());
        tvScreenName.setText("@"+u.getScreenName());
        tvFollowers.setText(u.getFollowersCount() + " ");
        tvFollowing.setText(u.getFollowingCount() + " ");
        if(u.isVerified()){
            ivVerified.setVisibility(View.VISIBLE);
        }
        Picasso.with(this).load(u.getProfileImageUrl()).into(ivProfileImage);
    }
}
