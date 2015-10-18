package com.codepath.apps.mysimpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class User implements Serializable{
    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;
    private String tagline;
    private int followersCount;
    private int followingCount;


    public String getTagline() {
        return tagline;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }


    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }



    public static User fromJSON (JSONObject jsonObject){
        User user  = new User();
        try {
            user.name = jsonObject.getString("name");
            user.uid = jsonObject.getLong("id");
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
            user.tagline = jsonObject.getString("description");
            user.followersCount = jsonObject.getInt("followers_count");
            user.followingCount = jsonObject.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  user;
    }

    public static User fromJson(JSONObject jsonUser) {
        User user = new User();

        try {
            user.name = jsonUser.getString("name");
            user.uid = jsonUser.getLong("id");
            user.screenName = jsonUser.getString("screen_name");
            user.profileImageUrl = jsonUser.getString("profile_image_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }
}
