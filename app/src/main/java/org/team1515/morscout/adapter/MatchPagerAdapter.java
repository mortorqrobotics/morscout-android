package org.team1515.morscout.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.team1515.morscout.fragment.main.HomeFragment;
import org.team1515.morscout.fragment.match.AllMatchesFragment;
import org.team1515.morscout.fragment.match.TeamScoutFragment;
import org.team1515.morscout.fragment.match.ViewDataFragment;

public class MatchPagerAdapter extends FragmentPagerAdapter {

    private int size;

    private Fragment scoutFrag,
            viewFrag,
            allMatchesFrag;

    public MatchPagerAdapter(FragmentManager manager) {
        super(manager);
        this.size = 3;

        scoutFrag = new TeamScoutFragment();
        viewFrag = new ViewDataFragment();
        allMatchesFrag = new AllMatchesFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return scoutFrag;
            case 1:
                return viewFrag;
            case 2:
                return allMatchesFrag;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Scout";
            case 1:
                return "View";
            case 2:
                return "All Matches";
            default:
                return "";
        }
    }

    @Override
    public int getCount() {
        return size;
    }
}
