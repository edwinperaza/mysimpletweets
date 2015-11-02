package com.codepath.apps.mysimpletweets.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.Activities.ProfileActivity;
import com.codepath.apps.mysimpletweets.Application.TwitterApplication;
import com.codepath.apps.mysimpletweets.Net.TwitterClient;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {


    public class TweetItemViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvBody;
        public TextView tvTimestamp;
        public TextView tvScreenName;
        public ImageView ivFavorite;
        public ImageView ivRetweet;
        public TextView tvCountRetweet;
        public TextView tvCountFavorite;
    }

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Tweet tweet = getItem(position);
        final int pos = position;
        final TweetItemViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            viewHolder = new TweetItemViewHolder();

            viewHolder.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
            viewHolder.tvScreenName = (TextView) convertView.findViewById(R.id.tvScreenName);
            viewHolder.tvBody = (TextView) convertView.findViewById(R.id.tvBody);
            viewHolder.tvTimestamp = (TextView) convertView.findViewById(R.id.tvTimestamp);
            viewHolder.ivFavorite = (ImageView) convertView.findViewById(R.id.ivFavorite);
            viewHolder.ivRetweet = (ImageView) convertView.findViewById(R.id.ivRetweet);
            viewHolder.tvCountRetweet = (TextView) convertView.findViewById(R.id.tvCountRetweet);
            viewHolder.tvCountFavorite = (TextView) convertView.findViewById(R.id.tvCountFavorite);


            viewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), ProfileActivity.class);
                    i.putExtra("user", tweet.getUser());
                    getContext().startActivity(i);

                }
            });

            viewHolder.ivFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long uid = tweet.getUid();
                    TwitterClient client;

                    if (tweet.isFavorited()) {
                        client = TwitterApplication.getRestClient();
                        client.deleteFavorite(uid, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                viewHolder.ivFavorite.setImageResource(R.drawable.ic_favorite);
                                tweet.setFavouritesCount(tweet.getFavouritesCount() - 1);
                                viewHolder.tvCountFavorite.setText(String.valueOf(tweet.getFavouritesCount()));
                                tweet.setFavorited(false);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                Log.d("ClickUnfavorite", errorResponse.toString());
                            }
                        });
                    } else {
                        client = TwitterApplication.getRestClient();
                        client.postFavorites(uid, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                viewHolder.ivFavorite.setImageResource(R.drawable.ic_favoriteon);
                                tweet.setFavouritesCount(tweet.getFavouritesCount()+1);
                                viewHolder.tvCountFavorite.setText(String.valueOf(tweet.getFavouritesCount()));
                                tweet.setFavorited(true);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                Log.d("ClickUnfavorite", errorResponse.toString());
                            }
                        });
                    }

                }
            });

            viewHolder.ivRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.ivRetweet.setImageResource(R.drawable.ic_retweeton);
                }

            });


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TweetItemViewHolder) convertView.getTag();
        }

        viewHolder.tvUserName.setText(tweet.getUser().getName());
        viewHolder.tvScreenName.setText("@" + tweet.getUser().getScreenName());
        viewHolder.tvBody.setText(tweet.getText());
        viewHolder.tvTimestamp.setText(getRelativeTimeAgo(tweet.getCreatedAt()));
        viewHolder.tvCountFavorite.setText(String.valueOf(tweet.getFavouritesCount()));
        viewHolder.tvCountRetweet.setText(String.valueOf(tweet.getRetweetCount()));
        if (tweet.isFavorited()) {
            viewHolder.ivFavorite.setImageResource(R.drawable.ic_favoriteon);
        }
        if (tweet.isRetweeted()) {
            viewHolder.ivRetweet.setImageResource(R.drawable.ic_retweeton);
        }

        viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext())
                .load(tweet.getUser().getProfileImageUrl())
                .into(viewHolder.ivProfileImage);


        return convertView;
    }


    private String getRelativeTimeAgo(String rawJsonDate) {

        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
