package org.team1515.morscout.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.team1515.morscout.R;

public class MainActivity extends AppCompatActivity {
    SharedPreferences preferences;
    RequestQueue queue;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(null, 0);
        queue = Volley.newRequestQueue(this);
    }
}
