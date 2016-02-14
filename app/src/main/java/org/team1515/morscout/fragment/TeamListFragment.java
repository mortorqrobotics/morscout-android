package org.team1515.morscout.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.team1515.morscout.R;
import org.team1515.morscout.adapter.TeamListAdapter;
import org.team1515.morscout.entity.Team;
import org.team1515.morscout.network.CookieRequest;

import java.util.ArrayList;
import java.util.List;

public class TeamListFragment extends Fragment {
    private RequestQueue queue;
    private SharedPreferences preferences;

    EditText searchTeams;
    String teamSearch;

    List<Team> teams;

    RecyclerView teamsList;
    TeamListAdapter teamListAdapter;
    LinearLayoutManager teamListManager;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teamlist, container, false);

        preferences = getActivity().getSharedPreferences(null, 0);
        queue = Volley.newRequestQueue(getContext());

        searchTeams = (EditText) view.findViewById(R.id.teamlist_searchbar);
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
                teamSearch = searchTeams.getText().toString();
            }
        });

        teams = new ArrayList<>();

        teamsList = (RecyclerView) view.findViewById(R.id.teamlist_list);
        teamListAdapter = new TeamListAdapter();
        teamListManager = new LinearLayoutManager(getContext());
        teamsList.setLayoutManager(teamListManager);
        teamsList.setAdapter(teamListAdapter);

        getTeams();

        return view;
    }

    public void getTeams() {
        CookieRequest requestTeams = new CookieRequest(Request.Method.POST, "/getTeamListForRegional", preferences, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    teams = new ArrayList<>();

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject team = jsonArray.getJSONObject(i);
                        teams.add(new Team(team.getString("key"), team.getString("team_number"), team.getString("nickname")));
                    }

                    sortTeams();

                    teamListAdapter.setTeams(teams);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "An error has occurred. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(requestTeams);
    }

    private void sortTeams() {
        // Bubble sort for now
        boolean hasChanged;
        do {
            hasChanged = false;
            for (int i = 0; i < teams.size() - 1; i++) {
                Team first = teams.get(i);
                Team last = teams.get(i + 1);
                if(Integer.parseInt(first.getNumber()) > Integer.parseInt(last.getNumber())) {
                    teams.set(i, last);
                    teams.set(i + 1, first);
                    hasChanged = true;
                }
            }
        } while(hasChanged);
    }
}
