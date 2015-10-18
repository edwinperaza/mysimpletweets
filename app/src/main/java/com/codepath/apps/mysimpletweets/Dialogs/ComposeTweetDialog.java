package com.codepath.apps.mysimpletweets.Dialogs;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.Net.TwitterClient;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.User;
import com.squareup.picasso.Picasso;

public class ComposeTweetDialog extends DialogFragment {

    private EditText etComposeTweet;
    private Button btTweet;
    private ImageView ivCancel;
    private TextView tvCharacters;
    private ComposeTweetDialogListener listener;
    private TwitterClient client;
    private User user;
    private static int MAX_COUNT = 140;

    public ComposeTweetDialog() {
    }

    public interface ComposeTweetDialogListener {
        void onFinishComposeDialog(String inputText);
    }

    public static ComposeTweetDialog newInstance(String title, User user) {
        ComposeTweetDialog dialog = new ComposeTweetDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putSerializable("user", user);
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_compose_tweet, container);

        Bundle args = getArguments();
        User user = (User) args.getSerializable("user");

        tvCharacters = (TextView) view.findViewById(R.id.tvCharacters);
        etComposeTweet = (EditText) view.findViewById(R.id.etComposeTweet);
        btTweet = (Button) view.findViewById(R.id.btTweet);
        ivCancel = (ImageView) view.findViewById(R.id.ivCancel);
        TextView tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        ImageView ivUserProfileImage = (ImageView) view.findViewById(R.id.ivUserProfile);

        tvUsername.setText(user.getName());
        etComposeTweet.requestFocus();
        Picasso.with(getContext()).load(user.getProfileImageUrl()).into(ivUserProfileImage);

        etComposeTweetValidate();
        listeners();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    private void etComposeTweetValidate(){

        if (etComposeTweet.getText().length() == 0) {
            btTweet.setClickable(false);
            btTweet.setBackgroundColor(getResources().getColor(R.color.button_tweet_disabled));
        }

        etComposeTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int count = MAX_COUNT - s.length();
                if (count < 0) {
                    tvCharacters.setTextColor(Color.RED);
                    btTweet.setClickable(false);
                    btTweet.setBackgroundColor(getResources().getColor(R.color.button_tweet_disabled));
                } else {
                    tvCharacters.setTextColor(Color.BLACK);
                    btTweet.setClickable(true);
                    btTweet.setBackgroundColor(getResources().getColor(R.color.button_tweet_enable));
                }

                if (s.length() == 0) {
                    btTweet.setClickable(false);
                    btTweet.setBackgroundColor(getResources().getColor(R.color.button_tweet_disabled));
                }
                tvCharacters.setText(String.valueOf(count));
            }
        });
    }

    private void listeners(){
        btTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFinishComposeDialog(etComposeTweet.getText().toString());
                dismiss();
            }
        });

        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
