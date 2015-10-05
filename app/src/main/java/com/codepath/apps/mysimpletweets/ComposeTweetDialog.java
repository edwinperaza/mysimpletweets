package com.codepath.apps.mysimpletweets;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.User;
import com.squareup.picasso.Picasso;

public class ComposeTweetDialog extends DialogFragment {

    private EditText etComposeTweet;
    private Button btTweet;
    private ComposeTweetDialogListener listener;

    public ComposeTweetDialog() {
    }

    public interface ComposeTweetDialogListener {
        void onFinishComposeDialog(String inputText);
    }

    public static ComposeTweetDialog newInstance(String title, User user) {
        ComposeTweetDialog dialog = new ComposeTweetDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putSerializable("user",user);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ComposeTweetDialogListener) {
            listener = (ComposeTweetDialogListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement ComposeTweetDialog.onFinishComposeDialog");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_compose_tweet, container);

        Bundle args = getArguments();
        String title = args.getString("title", "Compose Tweet");
        User user = (User) args.getSerializable("user");
        getDialog().setTitle(title);

        etComposeTweet = (EditText) view.findViewById(R.id.etComposeTweet);
        etComposeTweet.requestFocus();
        btTweet = (Button) view.findViewById(R.id.btTweet);

        TextView tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        tvUsername.setText(user.getScreenName());

        ImageView ivUserProfileImage = (ImageView) view.findViewById(R.id.ivUserProfile);
        Picasso.with(getContext())
                .load(user.getProfileImageUrl())
                .into(ivUserProfileImage);

        btTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFinishComposeDialog(etComposeTweet.getText().toString());
                dismiss();
            }
        });

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return view;
    }




}
