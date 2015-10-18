package com.codepath.apps.mysimpletweets.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.Adapters.FollowAdapter;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.User;

import java.util.ArrayList;
import java.util.List;

public class FollowListFragment extends Fragment {

    private List<User> users;
    private ListView lvFollow;
    private FollowAdapter followAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        users = new ArrayList<>();
        followAdapter = new FollowAdapter(getContext(), users);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follow_list, container, false);

        lvFollow = (ListView) view.findViewById(R.id.lv_follow_list);
        lvFollow.setAdapter(followAdapter);

        return view;
    }

    public void addAll(List<User> users) {
        this.followAdapter.addAll(users);
    }
}
