package org.team1515.morscout.activity;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.CharacterPickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.team1515.morscout.R;
import org.team1515.morscout.adapter.MatchPagerAdapter;
import org.team1515.morscout.adapter.TeamPagerAdapter;
import org.team1515.morscout.entity.Team;

public class MatchActivity extends AppCompatActivity {
    SharedPreferences preferences;
    RequestQueue queue;

    MatchPagerAdapter pagerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;

    Team currentTeam;

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

        // Create fragment viewpager
        pagerAdapter = new MatchPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.match_pager);
        viewPager.setAdapter(pagerAdapter);

        // Set up tabs for viewpager
        tabLayout = (TabLayout) findViewById(R.id.match_tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        // Set up team buttons
        View.OnClickListener teamClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabLayout.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
            }
        };

        TextView[] blueTeams = new TextView[3];
        blueTeams[0] = (TextView) findViewById(R.id.match_blueTeam1);
        blueTeams[1] = (TextView) findViewById(R.id.match_blueTeam2);
        blueTeams[2] = (TextView) findViewById(R.id.match_blueTeam3);

        String[] blueAlliance = getIntent().getStringArrayExtra("blueAlliance");
        for(int i = 0; i < 3; i++) {
            blueTeams[i].setText(blueAlliance[i]);
            blueTeams[i].setOnClickListener(teamClick);
        }

        TextView[] redTeams = new TextView[3];
        redTeams[0] = (TextView) findViewById(R.id.match_redTeam1);
        redTeams[1] = (TextView) findViewById(R.id.match_redTeam2);
        redTeams[2] = (TextView) findViewById(R.id.match_redTeam3);

        String[] redAlliance = getIntent().getStringArrayExtra("redAlliance");
        for(int i = 0; i < 3; i++) {
            redTeams[i].setText(redAlliance[i]);
            redTeams[i].setOnClickListener(teamClick);
        }
    }

}
