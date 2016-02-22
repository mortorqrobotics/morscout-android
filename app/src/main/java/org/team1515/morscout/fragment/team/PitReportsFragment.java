package org.team1515.morscout.fragment.team;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.team1515.morscout.R;
import org.team1515.morscout.network.CookieRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mortorq on 2/20/16.
 */
public class PitReportsFragment extends Fragment {
    private RequestQueue queue;
    private SharedPreferences preferences;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pitreports, container, false);

        preferences = getActivity().getSharedPreferences(null, 0);
        queue = Volley.newRequestQueue(getContext());

        getTeamReports();

        return view;
    }

    public void getTeamReports() {
        Map<String, String> params = new HashMap<>();
        params.put("teamNumber", "1515");
        params.put("context", "match");

        CookieRequest requestTeamReports = new CookieRequest(Request.Method.POST, "/getTeamReports", params, preferences, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("team reports: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "An error has occurred. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(requestTeamReports);
    }
}
