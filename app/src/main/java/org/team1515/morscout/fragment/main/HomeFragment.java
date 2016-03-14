package org.team1515.morscout.fragment.main;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.team1515.morscout.R;

public class HomeFragment extends Fragment {
    private int titleWidth = 70;

    private TextView title;
    private TextView message;

    private Typeface Exo2_thin;
    private Typeface Exo2_light;

    private String[] msgs = {"Welcome to MorScout!", "OurScout is MorScout Than YourScout", "Made With Fifty Shades of Orange", "MorPower, MorTeamwork, MorIngenuity, MorScout", "LessWork, MorScout"};
    private int randIndex;

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

        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        float screenWidth = displayMetrics.widthPixels;
        title.measure(0, 0);
        while(title.getMeasuredWidth() > screenWidth - 20) {
            title.measure(0, 0);
            title.setTextSize(--titleWidth);
        }

        return view;
    }
}
