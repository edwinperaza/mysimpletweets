package com.codepath.apps.mysimpletweets.Net;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;


public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1/";
	public static final String REST_CONSUMER_KEY = "I3LUMghLomJQN4o3zLqo1rFLp";
	public static final String REST_CONSUMER_SECRET = "AyuDW3b7SR6ygf1I2qzDzHyuC1nNsgK8PoRKlItbxxAHF08oqS";
	public static final String REST_CALLBACK_URL = "oauth://epsimpletweets";

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

    public void getHomeTimelineSince(long sinceId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("/statuses/home_timeline.json");

        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("since_id", sinceId);

        client.get(apiUrl, params, handler);
    }

    public void getHomeTimelineBefore(long maxId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("/statuses/home_timeline.json");

        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("max_id", maxId - 1);

        client.get(apiUrl, params, handler);
    }

    public void getMentionsTimeline( AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("/statuses/mentions_timeline.json");

        RequestParams params = new RequestParams();
        params.put("count", 25);
        //params.put("since_id", sinceId);

        client.get(apiUrl, params, handler);
    }

    public void postTweet(String tweet, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("/statuses/update.json");

        RequestParams params = new RequestParams();
        params.put("status", tweet);

        client.post(apiUrl, params, handler);
    }

    public void getUserTimeline(String screenName, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("/statuses/user_timeline.json");

        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);
        params.put("count", 25);

        client.get(apiUrl, params, handler);
    }

    public void getCurrentUser(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("/account/verify_credentials.json");

        RequestParams params = new RequestParams();
        params.put("include_entities", false);
        params.put("skip_status", true);
        params.put("include_email", false);

        client.get(apiUrl, params, handler);
    }


    public void getUserInfo(AsyncHttpResponseHandler handler){}

}