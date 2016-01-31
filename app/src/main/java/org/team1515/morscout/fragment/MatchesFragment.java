package org.team1515.morscout.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.team1515.morscout.R;

/**
 * Created by prozwood on 1/25/16.
 */
public class MatchesFragment extends Fragment {
    SearchView matchesSearch;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_matches, container, false);

            return view;
        }
}
