package org.team1515.morscout.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.team1515.morscout.fragment.match.AllMatchesFragment;
import org.team1515.morscout.fragment.match.ScoutFragment;
import org.team1515.morscout.fragment.match.ViewFragment;

public class MatchPagerAdapter extends FragmentStatePagerAdapter {

    private int size;
    private int team;
    private int match;

    public MatchPagerAdapter(FragmentManager manager) {
        super(manager);
        this.size = 3;
        team = 0;
        match = 0;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;

        switch (position) {
            case 0:
                fragment = new ScoutFragment();
                break;
            case 1:
                fragment = new ViewFragment();
                break;
            case 2:
                fragment = new AllMatchesFragment();
                break;
            default:
                fragment = null;
        }

        if(fragment!= null) {
            Bundle bundle = new Bundle();
            bundle.putInt("team", team);
            bundle.putInt("match", match);
            fragment.setArguments(bundle);
        }

        return fragment;
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

    public void setTeam(int team) {
        this.team = team;
    }

    public void setMatch(int match) {
        this.match = match;
    }
}
