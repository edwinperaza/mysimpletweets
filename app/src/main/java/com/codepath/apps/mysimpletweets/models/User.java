package com.codepath.apps.mysimpletweets.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Table(name = "Users")
public class User extends Model implements Serializable{
    @Column(name = "remoteId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String uid;
    @Column(name = "name")
    private String name;
    @Column(name = "screenName")
    private String screenName;
    @Column(name = "profileImageUrl")
    private String profileImageUrl;
    @Column(name = "profileBackgroundImageUrl")
    private String profileBackgroundImageUrl;
    @Column(name = "description")
    private String tagline;
    @Column(name = "statusesCount")
    private int statusesCount;
    @Column(name = "followersCount")
    private int followersCount;
    @Column(name = "followingCount")
    private int followingCount;
    @Column(name = "verified")
    private boolean verified;

    public User() {
        super();
    }


    public static User fromJSON (JSONObject jsonObject){
        User user  = new User();
        try {
            user.uid = jsonObject.getString("id");
            user.name = jsonObject.getString("name");
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
            user.profileBackgroundImageUrl = jsonObject.getString("profileBackgroundImageUrl");
            user.tagline = jsonObject.getString("description");
            user.statusesCount = jsonObject.getInt("statusesCount");
            user.followersCount = jsonObject.getInt("followers_count");
            user.followingCount = jsonObject.getInt("friends_count");
            user.verified = jsonObject.getBoolean("verified");
            user.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  user;
    }

    public static User getById(String userId) {
        return new Select()
                .from(User.class)
                .where("id = ?", userId)
                .executeSingle();
    }

    public static List<User> fromJSONArray(JSONArray jsonArray) {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                // Convert from json
                JSONObject userJson = jsonArray.getJSONObject(i);
                User user = User.fromJSON(userJson);

                if (user != null) {
                    users.add(user);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return users;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getProfileBackgroundImageUrl() {
        return profileBackgroundImageUrl;
    }

    public String getTagline() {
        return tagline;
    }

    public int getStatusesCount() {
        return statusesCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public boolean isVerified() {
        return verified;
    }
}
