package org.team1515.morscout.activity;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.team1515.morscout.R;

public class TeamActivity extends AppCompatActivity {
    SharedPreferences preferences;
    RequestQueue queue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        preferences = getSharedPreferences(null, 0);
        queue = Volley.newRequestQueue(this);

        // Set up action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Exo2-Medium.ttf");
        toolbarTitle.setTypeface(typeface);
    }

}
