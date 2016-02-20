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

/**
 * Created by prozwood on 1/25/16.
 */
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
                        intent.putExtra("matchId", matches.get(position).getId());
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
                try {
                    matches = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(response);

                    //Create match list
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject match = jsonArray.getJSONObject(i);

                        JSONObject alliances = match.getJSONObject("alliances");

                        JSONObject blue = alliances.getJSONObject("blue");
                        JSONObject red = alliances.getJSONObject("red");

                        JSONArray blueAlliance = blue.getJSONArray("teams");
                        JSONArray redAlliance = red.getJSONArray("teams");

                        if(match.getString("comp_level").equals("qm")) {
                            if (!matchSearch.trim().isEmpty() && (match.getString("match_number").toLowerCase().contains(matchSearch) || blueAlliance.toString().contains(matchSearch) || redAlliance.toString().contains(matchSearch))) {
                                matches.add(new Match(match.getString("key"), "Match " + match.getString("match_number"), match.getString("comp_level"), blueAlliance, redAlliance));
                            } else if (matchSearch.trim().isEmpty()) {
                                matches.add(new Match(match.getString("key"), "Match " + match.getString("match_number"), match.getString("comp_level"), blueAlliance, redAlliance));
                            }
                        }
                    }

                    if (matches.size() == 0) {
                        Toast.makeText(getContext(), "No matches with that info have been found.", Toast.LENGTH_SHORT).show();
                        return;
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
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "An error has occurred. Please try again later.", Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);
            }
        });
        queue.add(requestMatches);
    }

    private void sortMatches() {
        // Bubble sort for now
        boolean hasChanged;
        do {
            hasChanged = false;
            for (int i = 0; i < matches.size() - 1; i++) {
                Match first = matches.get(i);
                Match last = matches.get(i + 1);
                if (Integer.parseInt(first.getName().substring(6)) > Integer.parseInt(last.getName().substring(6))) {
                    matches.set(i, last);
                    matches.set(i + 1, first);
                    hasChanged = true;
                }
            }
        } while (hasChanged);
    }
}
