package org.team1515.morscout.fragment.match;

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

import org.json.JSONArray;
import org.json.JSONObject;
import org.team1515.morscout.R;
import org.team1515.morscout.network.CookieRequest;

public class ViewFragment extends Fragment {
    private RequestQueue queue;
    private SharedPreferences preferences;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewmatchscout, container, false);

        preferences = getActivity().getSharedPreferences(null, 0);
        queue = Volley.newRequestQueue(getContext());

        getMatchReports();

        return view;
    }

    public void getMatchReports() {
        CookieRequest requestMatchReports = new CookieRequest(Request.Method.POST, "/getMatchReports", preferences, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject reports = new JSONObject(response);
                    JSONArray yourTeam = reports.getJSONArray("yourTeam");
                    JSONArray otherTeams = reports.getJSONArray("otherTeams");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "An error has occurred. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(requestMatchReports);
    }
}
