package org.team1515.morscout.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.team1515.morscout.fragment.main.HomeFragment;
import org.team1515.morscout.fragment.match.MatchScoutFragment;
import org.team1515.morscout.fragment.match.TeamScoutFragment;
import org.team1515.morscout.fragment.team.GalleryFragment;
import org.team1515.morscout.fragment.team.MatchReportsFragment;
import org.team1515.morscout.fragment.team.PitReportsFragment;
import org.team1515.morscout.fragment.team.RegionalResultsFragment;
import org.team1515.morscout.fragment.team.TeamStatsFragment;

public class TeamPagerAdapter extends FragmentPagerAdapter {

    private int size;

    private Fragment scoutFrag,
            pitReportsFrag,
            matchReportsFrag,
            galleryFrag,
            regionalResultsFrag,
            teamStatsFrag;

    public TeamPagerAdapter(FragmentManager manager) {
        super(manager);
        this.size = 6;

        scoutFrag = new TeamScoutFragment();
        pitReportsFrag = new PitReportsFragment();
        matchReportsFrag = new MatchReportsFragment();
        galleryFrag = new GalleryFragment();
        regionalResultsFrag = new RegionalResultsFragment();
        teamStatsFrag = new TeamStatsFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return scoutFrag;
            case 1:
                return pitReportsFrag;
            case 2:
                return matchReportsFrag;
            case 3:
                return galleryFrag;
            case 4:
                return regionalResultsFrag;
            case 5:
                return teamStatsFrag;
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
            case 2:
                return "Match Reports";
            case 3:
                return "Gallery";
            case 4:
                return "Regional Results";
            case 5:
                return "Team Stats";
            default:
                return "";
        }
    }

    @Override
    public int getCount() {
        return size;
    }
}
