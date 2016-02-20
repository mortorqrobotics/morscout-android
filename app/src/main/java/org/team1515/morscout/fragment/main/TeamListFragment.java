package org.team1515.morscout.fragment.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import org.team1515.morscout.activity.MainActivity;
import org.team1515.morscout.activity.TeamActivity;
import org.team1515.morscout.adapter.RecyclerItemClickListener;
import org.team1515.morscout.adapter.TeamListAdapter;
import org.team1515.morscout.entity.Team;
import org.team1515.morscout.network.CookieRequest;

import java.util.ArrayList;
import java.util.List;

public class TeamListFragment extends Fragment {
    private RequestQueue queue;
    private SharedPreferences preferences;

    ProgressBar progress;

    EditText searchTeams;
    String teamSearch;

    List<Team> teams;

    RecyclerView teamsList;
    TeamListAdapter teamListAdapter;
    LinearLayoutManager teamListManager;

    SwipeRefreshLayout refreshLayout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teamlist, container, false);

        preferences = getActivity().getSharedPreferences(null, 0);
        queue = Volley.newRequestQueue(getContext());

        progress = (ProgressBar) view.findViewById(R.id.teamList_loading);
        progress.getIndeterminateDrawable().setColorFilter(Color.rgb(255, 197, 71), android.graphics.PorterDuff.Mode.MULTIPLY);

        teamSearch = "";
        searchTeams = (EditText) view.findViewById(R.id.teamlist_searchbar);
        searchTeams.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                teamSearch = searchTeams.getText().toString();
                getTeams();
            }
        });

        teams = new ArrayList<>();

        teamsList = (RecyclerView) view.findViewById(R.id.teamlist_list);
        teamListAdapter = new TeamListAdapter();
        teamListManager = new LinearLayoutManager(getContext());
        teamsList.setLayoutManager(teamListManager);
        teamsList.setAdapter(teamListAdapter);

        teamsList.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getContext(), TeamActivity.class);
                        intent.putExtra("teamId", teams.get(position).getId());
                        startActivity(intent);
                    }
                })
        );

        teamsList.setVisibility(View.GONE);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.teamlist_refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        refreshLayout.setRefreshing(false);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTeams();
            }
        });

        getTeams();

        return view;
    }

    public void getTeams() {
        progress.setVisibility(View.VISIBLE);
        teamsList.setVisibility(View.GONE);

        CookieRequest requestTeams = new CookieRequest(Request.Method.POST, "/getTeamListForRegional", preferences, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    teams = new ArrayList<>();

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject team = jsonArray.getJSONObject(i);
                        if (!teamSearch.trim().isEmpty() && (team.getString("team_number").toLowerCase().contains(teamSearch) || team.getString("nickname").toLowerCase().contains(teamSearch))) {
                            teams.add(new Team(team.getString("key"), team.getString("team_number"), team.getString("nickname")));
                        } else if (teamSearch.trim().isEmpty()) {
                            teams.add(new Team(team.getString("key"), team.getString("team_number"), team.getString("nickname")));
                        }
                    }

                    if (teams.size() == 0) {
                        Toast.makeText(getContext(), "No teams with that info have been found.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    sortTeams();

                    progress.setVisibility(View.GONE);
                    teamsList.setVisibility(View.VISIBLE);

                    teamListAdapter.setTeams(teams);
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    refreshLayout.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "An error has occurred. Please try again later.", Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);
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
                if (Integer.parseInt(first.getNumber()) > Integer.parseInt(last.getNumber())) {
                    teams.set(i, last);
                    teams.set(i + 1, first);
                    hasChanged = true;
                }
            }
        } while (hasChanged);
    }
}