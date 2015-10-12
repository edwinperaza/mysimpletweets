package com.codepath.apps.mysimpletweets.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.mysimpletweets.Fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.Fragments.MentionsTimelineFragment;

public class TweetsPageAdapter extends FragmentPagerAdapter {

    private static final String TABLE_TITLES[] = { "Home" , "Mentions" };

    public TweetsPageAdapter (FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new HomeTimelineFragment();
        } else if (position == 1) {
            return new MentionsTimelineFragment();
        } else {
            return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TABLE_TITLES[position];
    }

    @Override
    public int getCount() {
        return TABLE_TITLES.length;
    }
}
