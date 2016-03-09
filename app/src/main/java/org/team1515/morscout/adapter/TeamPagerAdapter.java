package org.team1515.morscout.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.team1515.morscout.fragment.team.PitReportsFragment;
import org.team1515.morscout.fragment.team.TeamScoutFragment;

public class TeamPagerAdapter extends FragmentPagerAdapter {

    private int size;

    private int team;

    public TeamPagerAdapter(FragmentManager manager) {
        super(manager);
        this.size = 2;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;

        switch (position) {
            case 0:
                fragment = new TeamScoutFragment();
                break;
            case 1:
                fragment = new PitReportsFragment();
                break;
            default:
                fragment = null;
        }

        if(fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("team", team);
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
                return "Pit Reports";
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
}
