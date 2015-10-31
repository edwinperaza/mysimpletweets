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
    @Column(name = "RemoteId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid;
    @Column(name = "Name")
    private String name;
    @Column(name = "ScreenName")
    private String screenName;
    @Column(name = "ProfileImageUrl")
    private String profileImageUrl;
    @Column(name = "ProfileBackgroundImageUrl")
    private String profileBackgroundImageUrl;
    @Column(name = "Description")
    private String tagline;
    @Column(name = "StatusesCount")
    private int statusesCount;
    @Column(name = "FollowersCount")
    private int followersCount;
    @Column(name = "FollowingCount")
    private int followingCount;
    @Column(name = "Verified")
    private boolean verified;


    @Column(name= "Current")
    private boolean current;


    public User() {
        super();
    }

    public static User saveCurrentUser(JSONObject userObject, boolean isCurrent) {
        User u  = User.fromJSON(userObject);
        u.current = isCurrent;
        u.save();
        return u;
    }

    public static User fromJSON (JSONObject jsonObject){
        User user  = new User();
        try {
            user.uid = jsonObject.getLong("id");
            user.name = jsonObject.getString("name");
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
            user.profileBackgroundImageUrl = jsonObject.getString("profile_background_image_url");
            user.tagline = jsonObject.getString("description");
            user.statusesCount = jsonObject.getInt("statuses_count");
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

    public static User getCurrent() {
        return new Select()
                .from(User.class)
                .where("Current = ?", 1)
                .orderBy("RANDOM()")
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

    public long getUid() {
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

    public boolean isCurrent() {
        return current;
    }

}
