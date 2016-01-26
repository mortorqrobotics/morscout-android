package org.team1515.morscout.activity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.team1515.morscout.R;
import org.team1515.morscout.fragment.FeedbackFragment;
import org.team1515.morscout.fragment.HelpFragment;
import org.team1515.morscout.fragment.HomeFragment;
import org.team1515.morscout.fragment.MatchesFragment;
import org.team1515.morscout.fragment.SettingsFragment;
import org.team1515.morscout.fragment.TeamFragment;
import org.team1515.morscout.fragment.TeamListFragment;
import org.team1515.morscout.network.ImageCookieRequest;

public class MainActivity extends AppCompatActivity {
    SharedPreferences preferences;
    RequestQueue queue;

    PopupMenu popupMenu;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(null, 0);
        queue = Volley.newRequestQueue(this);

        //Set up action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set up action bar profile picture
        final ImageButton profilePic = (ImageButton) toolbar.findViewById(R.id.actionbar_pic);
        profilePic.setClickable(true);
        profilePic.setVisibility(View.VISIBLE);
        ImageCookieRequest profilePicRequest = new ImageCookieRequest("http://www.morteam.com" + preferences.getString("profpicpath", "") + "-60",
                preferences, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                profilePic.setImageBitmap(response);
            }
        }, 0, 0, null, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        queue.add(profilePicRequest);
    }

    private class SectionPagerAdapter extends FragmentPagerAdapter {

        public HomeFragment homeFragment;
        public MatchesFragment matchesFragment;
        public TeamListFragment teamListFragment;
        public TeamFragment teamFragment;
        public SettingsFragment settingsFragment;
        public HelpFragment helpFragment;
        public FeedbackFragment feedbackFragment;

        public SectionPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);

            homeFragment = new HomeFragment();
            matchesFragment = new MatchesFragment();
            teamListFragment = new TeamListFragment();
            teamFragment = new TeamFragment();
            settingsFragment = new SettingsFragment();
            helpFragment = new HelpFragment();
            feedbackFragment = new FeedbackFragment();
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return homeFragment;
                case 1:
                    return matchesFragment;
                case 2:
                    return teamListFragment;
                case 3:
                    return teamFragment;
                case 4:
                    return settingsFragment;
                case 5:
                    return helpFragment;
                case 6:
                    return feedbackFragment;
                default:
                    return new Fragment();
            }
        }

        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Home";
                case 1:
                    return "All Matches";
                case 2:
                    return "Team List";
                case 3:
                    return "Sync";
                case 4:
                    return "Settings";
                case 5:
                    return "Help";
                case 6:
                    return "Feedback";
                default:
                    return "Home";
            }
        }
    }
}
