package org.team1515.morscout.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
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
 * Created by prozwood on 1/25/16.
 */
public class TeamListFragment extends Fragment {
    private RequestQueue queue;
    private SharedPreferences preferences;

    EditText searchTeams;
    String teamNumber;

    TableLayout teamsTable;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teamlist, container, false);

        preferences = getActivity().getSharedPreferences(null, 0);
        queue = Volley.newRequestQueue(getContext());

        searchTeams = (EditText) view.findViewById(R.id.teams_searchbar);
        searchTeams.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                teamNumber = searchTeams.getText().toString();
            }
        });

        teamsTable = (TableLayout) view.findViewById(R.id.teams_table);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getTeams();
    }

    public void getTeams() {
        Map<String, String> params = new HashMap<>();
        params.put("teamCode", "partedhair");

        CookieRequest requestTeams = new CookieRequest(Request.Method.POST, "/getTeamListForRegional", params, preferences, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "An error has ocurred. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(requestTeams);
    }
}
