package org.team1515.morscout.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.team1515.morscout.entity.Team;
import org.team1515.morscout.fragment.ScoutFragment;
import org.team1515.morscout.fragment.ViewReportFragment;
import org.team1515.morscout.fragment.team.InfoFragment;

public class TeamPagerAdapter extends FragmentPagerAdapter {

    private int size;

    private Team team;

    public TeamPagerAdapter(FragmentManager manager) {
        super(manager);
        this.size = 3;
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
                fragment = new InfoFragment();
                bundle.putString("url", team.getUrl());
                bundle.putString("location", team.getLocation());
                bundle.putString("sponsors", team.getSponsors());
                break;
            default:
                fragment = null;
        }

        if(fragment != null) {
            bundle.putString("context", "pit");
            bundle.putInt("team", team.getNumber());
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
            case 2:
                return "Info";
            default:
                return "";
        }
    }

    @Override
    public int getCount() {
        return size;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
