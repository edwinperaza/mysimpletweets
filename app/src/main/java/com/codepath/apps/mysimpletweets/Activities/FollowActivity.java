package com.codepath.apps.mysimpletweets.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.codepath.apps.mysimpletweets.Fragments.FollowerFragment;
import com.codepath.apps.mysimpletweets.Fragments.FollowingFragment;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.User;

public class FollowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        // Get the user and type from the intent
        User user = (User) getIntent().getSerializableExtra("user");
        String type = getIntent().getStringExtra("type");

        if (type.equals("followers")) {
            // Followers
            FollowerFragment followerFragment = FollowerFragment.newInstance(user);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_follow, followerFragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(R.string.Followers);
        } else {
            // Following
            FollowingFragment followingFragment = FollowingFragment.newInstance(user);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_follow, followingFragment);
            fragmentTransaction.commit();

            getSupportActionBar().setTitle(R.string.Following);
        }

    }
}
