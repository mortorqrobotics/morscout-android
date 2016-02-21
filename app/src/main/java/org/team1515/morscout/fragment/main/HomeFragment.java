package org.team1515.morscout.fragment.main;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.team1515.morscout.R;

public class HomeFragment extends Fragment {
    TextView title;
    TextView message;

    Typeface Exo2_thin;
    Typeface Exo2_light;

    String[] msgs = {"Welcome to MorScout!", "OurScout is MorScout Than YourScout", "Made With Fifty Shades of Orange", "MorPower, MorTeamwork, MorIngenuity, MorScout", "LessWork, MorScout"};
    int randIndex;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        randIndex = (int) Math.floor(Math.random() * msgs.length);

        Exo2_thin = Typeface.createFromAsset(getActivity().getAssets(), "Exo2-Thin.ttf");
        Exo2_light = Typeface.createFromAsset(getActivity().getAssets(), "Exo2-Light.ttf");

        title = (TextView) view.findViewById(R.id.home_title);
        title.setTypeface(Exo2_light);

        message = (TextView) view.findViewById(R.id.home_message);
        message.setTypeface(Exo2_thin);
        message.setText(msgs[randIndex]);

        return view;
    }
}
