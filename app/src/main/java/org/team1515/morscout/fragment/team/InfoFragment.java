package org.team1515.morscout.fragment.team;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;

import org.team1515.morscout.R;

public class InfoFragment extends Fragment {

    TextView websiteView;
    TextView locationView;
    TextView sponsorView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        websiteView = (TextView) view.findViewById(R.id.info_website);
        websiteView.setText(getArguments().getString("url"));
        locationView = (TextView) view.findViewById(R.id.info_location);
        locationView.setText(getArguments().getString("location"));
        sponsorView = (TextView) view.findViewById(R.id.info_sponsors);
        sponsorView.setText(getArguments().getString("sponsors"));

        return view;
    }
}
