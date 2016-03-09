package org.team1515.morscout.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.team1515.morscout.R;
import org.team1515.morscout.adapter.MatchPagerAdapter;
import org.team1515.morscout.entity.FormItem;
import org.team1515.morscout.entity.Match;

import java.util.List;
import java.util.Map;

public class MatchActivity extends AppCompatActivity {
    SharedPreferences preferences;
    RequestQueue queue;

    MatchPagerAdapter pagerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;

    TextView[] blueTeamViews;
    TextView[] redTeamViews;

    Match match;

    private int currentTeam;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        preferences = getSharedPreferences(null, 0);
        queue = Volley.newRequestQueue(this);

        // Set up action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Exo2-Medium.ttf");
        toolbarTitle.setTypeface(typeface);

        match = new Gson().fromJson(getIntent().getStringExtra("match"), Match.class);

        // Create match title
        TextView title = (TextView) findViewById(R.id.match_title);
        title.setText("Match " + match.getNumber());

        // Create fragment viewpager
        viewPager = (ViewPager) findViewById(R.id.match_pager);
        pagerAdapter = new MatchPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        // Set up tabs for viewpager
        tabLayout = (TabLayout) findViewById(R.id.match_tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        // Set up team buttons
        currentTeam = 0;

        View.OnClickListener redTeamClick = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Highlight current team
                for (TextView teamView : blueTeamViews) {
                    teamView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.blueAlliance));
                }
                for (TextView teamView : redTeamViews) {
                    teamView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.redAlliance));
                }
                view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.redAllianceSelected));

                currentTeam = Integer.parseInt(((TextView) view).getText().toString());

                pagerAdapter.setTeam(currentTeam);
                pagerAdapter.setMatch(match.getNumber());
                pagerAdapter.setRegional(match.getId());

                viewPager.setAdapter(pagerAdapter);

                tabLayout.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
            }
        };

        View.OnClickListener blueTeamClick = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Highlight current team
                for (TextView teamView : blueTeamViews) {
                    teamView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.blueAlliance));
                }
                for (TextView teamView : redTeamViews) {
                    teamView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.redAlliance));
                }
                view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.blueAllianceSelected));

                currentTeam = Integer.parseInt(((TextView) view).getText().toString());

                pagerAdapter.setTeam(currentTeam);
                pagerAdapter.setMatch(match.getNumber());
                pagerAdapter.setRegional(match.getId());

                viewPager.setAdapter(pagerAdapter);

                tabLayout.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
            }
        };


        blueTeamViews = new TextView[3];
        blueTeamViews[0] = (TextView) findViewById(R.id.match_blueTeam1);
        blueTeamViews[1] = (TextView) findViewById(R.id.match_blueTeam2);
        blueTeamViews[2] = (TextView) findViewById(R.id.match_blueTeam3);

        String[] blueAlliance = match.getBlueAlliance();

        for (int i = 0; i < 3; i++) {
            blueTeamViews[i].setText(blueAlliance[i]);
            blueTeamViews[i].setOnClickListener(blueTeamClick);
            if(blueAlliance[i].equals(preferences.getString("teamNumber", ""))) {
                pagerAdapter.setSize(2);
            }
        }

        redTeamViews = new TextView[3];
        redTeamViews[0] = (TextView) findViewById(R.id.match_redTeam1);
        redTeamViews[1] = (TextView) findViewById(R.id.match_redTeam2);
        redTeamViews[2] = (TextView) findViewById(R.id.match_redTeam3);

        String[] redAlliance = match.getRedAlliance();
        for (int i = 0; i < 3; i++) {
            redTeamViews[i].setText(redAlliance[i]);
            redTeamViews[i].setOnClickListener(redTeamClick);
            if(redAlliance[i].equals(preferences.getString("teamNumber", ""))) {
                pagerAdapter.setSize(2);
            }
        }
    }
}
