package org.team1515.morscout.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.team1515.morscout.R;
import org.team1515.morscout.activity.TeamActivity;
import org.team1515.morscout.adapter.RecyclerItemClickListener;
import org.team1515.morscout.adapter.TeamListAdapter;
import org.team1515.morscout.entity.Team;
import org.team1515.morscout.network.CookieRequest;
import org.team1515.morscout.network.NetworkUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.view.View.VISIBLE;
import static org.team1515.morscout.MorScout.preferences;

public class TeamListFragment extends EntityList {
    private List<Team> teams;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setType("pit");

        View view = super.onCreateView(inflater, container);

        setSearchListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String teamSearch = s.toString().toLowerCase();

                if (!teamSearch.trim().isEmpty()) {
                    List<Team> searchedTeams = new ArrayList<>();
                    for (Team team : teams) {
                        if (team.getName().toLowerCase().contains(teamSearch) || Integer.toString(team.getNumber()).contains(teamSearch)) {
                            searchedTeams.add(team);
                        }
                    }
                    ((TeamListAdapter) adapter).setTeams(searchedTeams);
                } else {
                    ((TeamListAdapter) adapter).setTeams(teams);
                }
                adapter.notifyDataSetChanged();
            }
        });

        setItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getContext(), TeamActivity.class);
                TextView teamNumView = (TextView) view.findViewById(R.id.teamlist_teamNumber);
                int teamNum = Integer.parseInt((teamNumView.getText().toString()));
                for (Team team : teams) {
                    if (team.getNumber() == teamNum) {
                        intent.putExtra("team", new Gson().toJson(team));
                        break;
                    }
                }
                startActivity(intent);
            }
        }));

        return view;
    }

    protected void initAdapter() {
        adapter = new TeamListAdapter();
    }

    @Override
    protected void getProgress() {
        CookieRequest requestProgress = new CookieRequest(Request.Method.POST,
                NetworkUtils.makeMorScoutURL("/getProgressForPit", true),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            preferences.edit().putString("teamProgress", response).apply();

                            JSONObject progressObj = new JSONObject(response);
                            for (Team team : teams) {
                                team.setProgress(progressObj.getInt(team.getNumber() + ""));
                            }
                            ((TeamListAdapter) adapter).setTeams(teams);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        queue.add(requestProgress);
    }

    @Override
    protected void processEntities(String teamStr) {
        teams = new ArrayList<>();

        try {
            teams = new ArrayList<>();

            JSONArray jsonArray = new JSONArray(teamStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject team = jsonArray.getJSONObject(i);
                teams.add(new Team(team.getString("key"), team.getInt("team_number"), team.getString("nickname"), team.getString("website"), team.getString("locality"), team.getString("name")));
            }

            sortTeams();
            ((TeamListAdapter) adapter).setTeams(teams);
            setListVisibility(VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sortTeams() {
        Collections.sort(teams, new Comparator<Team>() {
            @Override
            public int compare(Team lhs, Team rhs) {
                return lhs.getNumber() - rhs.getNumber();
            }
        });
    }
}
