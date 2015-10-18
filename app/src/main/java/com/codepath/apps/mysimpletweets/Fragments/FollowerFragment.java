package com.codepath.apps.mysimpletweets.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.Application.TwitterApplication;
import com.codepath.apps.mysimpletweets.Net.TwitterClient;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class FollowerFragment extends FollowListFragment{

    private User user;
    private TwitterClient client;

    public static FollowerFragment newInstance(User user) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);

        FollowerFragment followerFragment = new FollowerFragment();
        followerFragment.setArguments(bundle);

        return followerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = (User) getArguments().getSerializable("user");
        client = TwitterApplication.getRestClient();

        populateFollowers();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void populateFollowers() {
        if (!isNetworkAvailable()) {
            return;
        }

        client.getFollowers(user.getScreenName(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray rawData = response.getJSONArray("users");
                    List<User> users = User.fromJSONArray(rawData);
                    addAll(users);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                Toast.makeText(getActivity(), getResources().getString(R.string.error_fetch_followers), Toast.LENGTH_LONG).show();
            }
        });
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean networkConnection = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        if (!networkConnection) {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
        return networkConnection;
    }
}
