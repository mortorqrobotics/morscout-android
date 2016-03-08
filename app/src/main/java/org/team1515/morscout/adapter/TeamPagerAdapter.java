package org.team1515.morscout.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.team1515.morscout.fragment.team.PitReportsFragment;
import org.team1515.morscout.fragment.team.TeamScoutFragment;

public class TeamPagerAdapter extends FragmentPagerAdapter {

    private int size;

    private Fragment scoutFrag,
            pitReportsFrag;

    public TeamPagerAdapter(FragmentManager manager) {
        super(manager);
        this.size = 2;

        scoutFrag = new TeamScoutFragment();
        pitReportsFrag = new PitReportsFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return scoutFrag;
            case 1:
                return pitReportsFrag;
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
                return "Pit Reports";
            default:
                return "";
        }
    }

    @Override
    public int getCount() {
        return size;
    }
}
