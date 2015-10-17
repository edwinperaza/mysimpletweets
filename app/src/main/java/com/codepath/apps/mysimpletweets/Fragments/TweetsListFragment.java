package com.codepath.apps.mysimpletweets.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.Activities.ProfileActivity;
import com.codepath.apps.mysimpletweets.Adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.Application.TwitterApplication;
import com.codepath.apps.mysimpletweets.Helpers.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.Net.TwitterClient;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;

import java.util.ArrayList;


public class TweetsListFragment extends Fragment {

    protected ArrayList<Tweet> tweets;
    protected TweetsArrayAdapter aTweets;

    private ListView lvTweets;

    protected TwitterClient client;
    protected User currentUser;
    protected long oldestTweetUid = Long.MAX_VALUE;

    private SwipeRefreshLayout swipeContainer;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tweets_list, parent, false);

        lvTweets = (ListView) view.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemCount) {
                populateTimeline(1, oldestTweetUid);
                swipeContainer.setRefreshing(false);
                return true;
            }
        });

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateNewTweetsTimeline();
                swipeContainer.setRefreshing(false);
            }
        });
        swipeConfigureColor();

        populateTimeline(1, oldestTweetUid);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);

        client = TwitterApplication.getRestClient();

        aTweets.setImageProfileClickListener(new TweetsArrayAdapter.OnImageProfileClickListener() {
            @Override
            public void onImageProfileClick(View itemView, int position) {
                Tweet tweet = aTweets.getItem(position);
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra("user", tweet.getUser());
                startActivity(intent);
            }
        });
    }

    public void populateNewTweetsTimeline() {}

    public void populateTimeline(long sinceId, long maxId) {}

    // Configure the refreshing colors
    public void swipeConfigureColor(){
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }


}
