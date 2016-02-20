package org.team1515.morscout.activity;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.team1515.morscout.R;
import org.team1515.morscout.adapter.MatchPagerAdapter;
import org.team1515.morscout.adapter.TeamPagerAdapter;

public class MatchActivity extends AppCompatActivity {
    SharedPreferences preferences;
    RequestQueue queue;

    MatchPagerAdapter pagerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;

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

        //Create fragment viewpager
        pagerAdapter = new MatchPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.match_pager);
        viewPager.setAdapter(pagerAdapter);

        //Set up tabs for viewpager
        tabLayout = (TabLayout) findViewById(R.id.match_tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

}
