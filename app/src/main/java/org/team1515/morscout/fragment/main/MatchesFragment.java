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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.team1515.morscout.R;
import org.team1515.morscout.activity.MatchActivity;
import org.team1515.morscout.adapter.MatchListAdapter;
import org.team1515.morscout.adapter.RecyclerItemClickListener;
import org.team1515.morscout.entity.Match;
import org.team1515.morscout.network.CookieRequest;

import java.util.ArrayList;
import java.util.List;

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
                // TODO Auto-generated method stub
                matchSearch = searchMatches.getText().toString();
                getMatches();
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
                        intent.putExtra("match", new Gson().toJson(matches.get(position), Match.class));
                        startActivity(intent);
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
    public void getMatches() {
        progress.setVisibility(View.VISIBLE);
        matchesList.setVisibility(View.GONE);

        CookieRequest requestMatches = new CookieRequest(Request.Method.POST, "/getMatchesForCurrentRegional", preferences, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                preferences.edit().putString("matches", response).apply();
                initMatches(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "An error has occurred. Please try again later.", Toast.LENGTH_SHORT).show();
                initMatches(preferences.getString("matches", "[]"));
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
                if(matchObject.getString("comp_level").equals("qm")) {

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

                    matches.add(new Match(matchObject.getString("key"), matchObject.getInt("match_number"), matchObject.getString("comp_level"), blueAlliance, redAlliance));
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
