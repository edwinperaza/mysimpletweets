package com.codepath.apps.mysimpletweets.models;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Table(name = "Tweets")
public class Tweet extends Model {
    @Column(name = "RemoteId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid;
    @Column(name = "Text")
    private String text;
    @Column(name= "CreateAt")
    private String createdAt;
    @Column(name= "Favorited")
    private boolean favorited;
    @Column(name = "Retweeted")
    private boolean retweeted;
    @Column(name = "Retweet_count")
    private int retweet_count;
    @Column(name = "Mention")
    private boolean mention;
    @Column(name = "User", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private User user;

    public Tweet() {
        super();
    }

    public static Tweet fromJSON (JSONObject jsonObject,boolean isMention){
        Tweet tweet = new Tweet();
        try {
            tweet.uid = jsonObject.getLong("id");
            tweet.text = jsonObject.getString("text");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.favorited = jsonObject.getBoolean("favorited");
            tweet.retweeted = jsonObject.getBoolean("retweeted");
            tweet.retweet_count = jsonObject.getInt("retweet_count");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            tweet.mention = isMention;
            Log.d("TWEEETS:",tweet.isMention() + tweet.user.getScreenName());
            tweet.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray, boolean isMention){
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(jsonObject,isMention);

                if (tweet != null){
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return tweets;
    }


    public static List<Tweet> getAllTimeline() {
        return new Select().from(Tweet.class).where("Mention = ?", 0).orderBy("RemoteId DESC")
                .execute();
    }

    public static List<Tweet> getAllMentions() {
        return new Select().from(Tweet.class).where("Mention = ?", 1).orderBy("RemoteId DESC")
                .execute();
    }

    public static List<Tweet> getUserTimeline(User user) {
        Log.d("DEBUUUUUUUG:",user.getId().toString());
        return new Select().from(Tweet.class).where("User = ?", user.getId())
                .orderBy("RemoteId DESC").execute();
    }


    public long getUid() {
        return uid;
    }

    public String getText() {
        return text;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public int getRetweet_count() {
        return retweet_count;
    }

    public User getUser() {
        return user;
    }

    public boolean isMention() {
        return mention;
    }


}
