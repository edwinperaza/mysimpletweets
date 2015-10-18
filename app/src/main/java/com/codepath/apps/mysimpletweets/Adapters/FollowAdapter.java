package com.codepath.apps.mysimpletweets.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FollowAdapter extends ArrayAdapter<User>{

    public FollowAdapter(Context context, List<User> objects) {
        super(context, 0, objects);
    }

    private static class ViewHolder {
        TextView tvScreenName;
        TextView tvName;
        ImageView ivAvatar;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_profile, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView)convertView.findViewById(R.id.tv_name);
            viewHolder.tvScreenName = (TextView)convertView.findViewById(R.id.tv_screenname);
            viewHolder.ivAvatar = (ImageView)convertView.findViewById(R.id.iv_avatar);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.tvName.setText(user.getName());
        viewHolder.tvScreenName.setText(user.getScreenName());
        Picasso.with(getContext()).load(user.getProfileImageUrl()).into(viewHolder.ivAvatar);

        return convertView;
    }

}
