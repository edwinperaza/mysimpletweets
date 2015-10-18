package com.codepath.apps.mysimpletweets.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.Activities.ProfileActivity;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

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
    }

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Tweet tweet = getItem(position);
        final int  pos = position;

        final TweetItemViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            viewHolder = new TweetItemViewHolder();

            viewHolder.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
            viewHolder.tvScreenName = (TextView) convertView.findViewById(R.id.tvScreenName);
            viewHolder.tvBody = (TextView) convertView.findViewById(R.id.tvBody);
            viewHolder.tvTimestamp = (TextView) convertView.findViewById(R.id.tvTimestamp);
            viewHolder.ivFavorite = (ImageView) convertView.findViewById(R.id.ivFavorite);
            viewHolder.ivRetweet = (ImageView) convertView.findViewById(R.id.ivRetweet);


            viewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), ProfileActivity.class);
                    i.putExtra("user", tweet.getUser());
                    getContext().startActivity(i);

                }
            });

            viewHolder.ivFavorite.setOnClickListener(new View.OnClickListener (){
                @Override
                public void onClick(View v) {
                        viewHolder.ivFavorite.setImageResource(R.drawable.ic_favoriteon);
                    }

            });

            viewHolder.ivRetweet.setOnClickListener(new View.OnClickListener (){
                @Override
                public void onClick(View v) {
                    viewHolder.ivRetweet.setImageResource(R.drawable.ic_retweeton);
                }

            });


            convertView.setTag(viewHolder);
        }else{
            viewHolder = (TweetItemViewHolder) convertView.getTag();
        }

        viewHolder.tvUserName.setText(tweet.getUser().getName());
        viewHolder.tvScreenName.setText("@"+tweet.getUser().getScreenName());
        viewHolder.tvBody.setText(tweet.getBody());
        viewHolder.tvTimestamp.setText(getRelativeTimeAgo(tweet.getCreatedAt()));

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
