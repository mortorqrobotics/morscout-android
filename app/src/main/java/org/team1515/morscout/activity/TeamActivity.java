package org.team1515.morscout.activity;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.team1515.morscout.R;
import org.team1515.morscout.adapter.TeamPagerAdapter;
import org.team1515.morscout.network.CookieRequest;

import java.util.HashMap;
import java.util.Map;

public class TeamActivity extends AppCompatActivity {
    SharedPreferences preferences;
    RequestQueue queue;

    TeamPagerAdapter pagerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;

    int team;
    String regional;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        preferences = getSharedPreferences(null, 0);
        queue = Volley.newRequestQueue(this);

        team = getIntent().getIntExtra("team", 0);
        regional = getIntent().getStringExtra("regional");

        // Set up action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Exo2-Medium.ttf");
        toolbarTitle.setTypeface(typeface);

        //Create fragment viewpager
        pagerAdapter = new TeamPagerAdapter(getSupportFragmentManager());
        pagerAdapter.setTeam(team);
        viewPager = (ViewPager) findViewById(R.id.team_pager);
        viewPager.setAdapter(pagerAdapter);

        //Set up tabs for viewpager
        tabLayout = (TabLayout) findViewById(R.id.team_tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        getTeamInfo();
    }

    private void getTeamInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("teamNumber", String.valueOf(team));

        CookieRequest teamRequest = new CookieRequest(Request.Method.POST,
                "/getTeamInfo",
                params,
                preferences,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject teamObj = new JSONObject(response);
                            String name = "Team " + team + " - " + teamObj.getString("nickname");

                            TextView nameView = (TextView) findViewById(R.id.team_name);
                            nameView.setText(name);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        queue.add(teamRequest);
    }

}
