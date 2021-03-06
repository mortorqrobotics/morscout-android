package org.team1515.morscout.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.team1515.morscout.fragment.ScoutFragment;
import org.team1515.morscout.fragment.ViewReportFragment;
import org.team1515.morscout.fragment.match.StrategyFragment;

public class MatchPagerAdapter extends FragmentStatePagerAdapter {

    private int size;
    private int team;
    private int match;
    private String regional;

    public MatchPagerAdapter(FragmentManager manager) {
        super(manager);
        this.size = 2;
        team = 0;
        match = 0;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        Bundle bundle = new Bundle();

        switch (position) {
            case 0:
                fragment = new ScoutFragment();
                break;
            case 1:
                fragment = new ViewReportFragment();
                break;
            case 2:
                fragment = new StrategyFragment();
                break;
            default:
                fragment = null;
        }

        if (fragment != null) {
            bundle.putString("context", "match");
            bundle.putInt("team", team);
            bundle.putInt("match", match);
            bundle.putString("regional", regional);
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
                return "Strategy";
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

    public void setRegional(String regional) {
        this.regional = regional;
    }

    public void setSize(int size) {
        this.size = size;
        notifyDataSetChanged();
    }
}
