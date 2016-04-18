package org.team1515.morscout.fragment.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.team1515.morscout.R;
import org.team1515.morscout.activity.MatchActivity;
import org.team1515.morscout.adapter.MatchListAdapter;
import org.team1515.morscout.adapter.RecyclerItemClickListener;
import org.team1515.morscout.entity.Match;
import org.team1515.morscout.network.CookieRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchesFragment extends Fragment {
    private RequestQueue queue;
    private SharedPreferences preferences;

    EditText searchMatches;
    String matchSearch;

    ProgressBar progress;

    List<Match> matches;

    RecyclerView matchesList;
    MatchListAdapter matchListAdapter;
    LinearLayoutManager matchLayoutManager;

    SwipeRefreshLayout refreshLayout;

    JSONObject matchProgress;

    int matchesLength = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matches, container, false);

        preferences = getActivity().getSharedPreferences(null, 0);
        queue = Volley.newRequestQueue(getContext());

        progress = (ProgressBar) view.findViewById(R.id.matchList_loading);
        progress.getIndeterminateDrawable().setColorFilter(Color.rgb(255, 197, 71), android.graphics.PorterDuff.Mode.MULTIPLY);

        matchSearch = "";
        searchMatches = (EditText) view.findViewById(R.id.matches_searchbar);
        searchMatches.addTextChangedListener(new TextWatcher() {

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
                matchSearch = s.toString().toLowerCase();

                if (!matchSearch.trim().isEmpty()) {
                    List<Match> searchedMatches = new ArrayList<>();
                    for (Match match : matches) {
                        boolean isAdded = false;
                        if (Integer.toString(match.getNumber()).contains(matchSearch)) {
                            searchedMatches.add(match);
                            isAdded = true;
                        }
                        if (!isAdded) {
                            for (String team : match.getBlueAlliance()) {
                                if (team.contains(matchSearch)) {
                                    searchedMatches.add(match);
                                    isAdded = true;
                                    break;
                                }
                            }

                            if (!isAdded) {
                                for (String team : match.getRedAlliance()) {
                                    if (team.contains(matchSearch)) {
                                        searchedMatches.add(match);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    matchListAdapter.setMatches(searchedMatches);
                } else {
                    matchListAdapter.setMatches(matches);
                }
                matchListAdapter.notifyDataSetChanged();
            }
        });

        matches = new ArrayList<>();

        matchesList = (RecyclerView) view.findViewById(R.id.matches_list);
        matchListAdapter = new MatchListAdapter();
        matchLayoutManager = new LinearLayoutManager(getContext());
        matchesList.setLayoutManager(matchLayoutManager);
        matchesList.setAdapter(matchListAdapter);

        matchesList.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), MatchActivity.class);
//                        intent.putExtra("match", new Gson().toJson(matches.get(position)));
                        int matchNum = Integer.parseInt(((TextView) view.findViewById(R.id.matchlist_matchNumber)).getText().toString().split(" ")[1]);
                        for (int i = 0; i < matches.size(); i++) {
                            if (matches.get(i).getNumber() == matchNum) {
                                intent.putExtra("match", new Gson().toJson(matches.get(i)));
                                startActivity(intent);
                                break;
                            }
                        }

                    }
                })
        );

        matchesList.setVisibility(View.GONE);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.matches_refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        refreshLayout.setRefreshing(false);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMatches();
            }
        });

        getMatches();

        return view;
    }

    int x = 0;

    public void getProgress() {
        Map<String, String> params = new HashMap<>();
        params.put("matchesLength", Integer.toString(matchesLength));

        CookieRequest requestProgress = new CookieRequest(Request.Method.POST, "/getProgressForMatches", params, preferences, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    matchProgress = new JSONObject(response);
                    initMatches(preferences.getString("matches", "[]"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getContext(), "An error has occurred. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(requestProgress);
    }

    public void getMatches() {
        progress.setVisibility(View.VISIBLE);
        matchesList.setVisibility(View.GONE);

        CookieRequest requestMatches = new CookieRequest(Request.Method.POST, "/getMatchesForCurrentRegional", preferences, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    preferences.edit().putString("matches", response).apply();

                    JSONArray responseArray = new JSONArray(response);

                    matchesLength = 0;

                    for (int i = 0; i < responseArray.length(); i++) {
                        matchesLength++;
                    }

                    getProgress();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getProgress();
                error.printStackTrace();
                Toast.makeText(getContext(), "An error has occurred. Please try again later.", Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);
            }
        });
        queue.add(requestMatches);
    }

    public void initMatches(String json) {
        try {
            matches = new ArrayList<>();
            JSONArray matchArray = new JSONArray(json);


            //Create match list
            for (int i = 0; i < matchArray.length(); i++) {
                JSONObject matchObject = matchArray.getJSONObject(i);

                //Add all qualifying matches
                if (matchObject.getString("comp_level").equals("qm")) {

                    JSONObject alliancesObject = matchObject.getJSONObject("alliances");

                    JSONArray blueArray = alliancesObject.getJSONObject("blue").getJSONArray("teams");
                    JSONArray redArray = alliancesObject.getJSONObject("red").getJSONArray("teams");

                    String[] blueAlliance = new String[3];
                    for (int j = 0; j < blueAlliance.length; j++) {
                        blueAlliance[j] = blueArray.getString(j).replaceAll("frc", "");
                    }

                    String[] redAlliance = new String[3];
                    for (int j = 0; j < redAlliance.length; j++) {
                        redAlliance[j] = redArray.getString(j).replaceAll("frc", "");
                    }

                    matches.add(new Match(matchObject.getString("key"), matchObject.getInt("match_number"), matchObject.getString("comp_level"), blueAlliance, redAlliance, matchProgress.getInt(matchObject.getString("match_number")), matchObject.getInt("time")));
                }
            }

            sortMatches();

            progress.setVisibility(View.GONE);
            matchesList.setVisibility(View.VISIBLE);

            matchListAdapter.setMatches(matches);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            refreshLayout.setRefreshing(false);
        }
    }

    private void sortMatches() {
        // Bubble sort for now
        boolean hasChanged;
        do {
            hasChanged = false;
            for (int i = 0; i < matches.size() - 1; i++) {
                Match first = matches.get(i);
                Match last = matches.get(i + 1);
                if (first.getNumber() > last.getNumber()) {
                    matches.set(i, last);
                    matches.set(i + 1, first);
                    hasChanged = true;
                }
            }
        } while (hasChanged);
    }
}
