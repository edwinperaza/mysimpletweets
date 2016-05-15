package com.codepath.apps.mysimpletweets.models;

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
    @Column(name= "favouritesCount")
    private int favouritesCount;
    @Column(name = "Retweeted")
    private boolean retweeted;
    @Column(name = "retweetCount")
    private int retweetCount;
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
            tweet.favouritesCount = jsonObject.getInt("favorite_count");
            tweet.retweeted = jsonObject.getBoolean("retweeted");
            tweet.retweetCount = jsonObject.getInt("retweet_count");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            tweet.mention = isMention;
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

    public static List<Tweet> getUserTimeline(long userId) {
       // Log.d("USER_ID", user.getId().toString());
        return new Select().from(Tweet.class).where("User = ?", userId)
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

    public int getFavouritesCount() {
        return favouritesCount;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public User getUser() {
        return user;
    }

    public boolean isMention() {
        return mention;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    public void setFavouritesCount(int favouritesCount) {
        this.favouritesCount = favouritesCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }
}
