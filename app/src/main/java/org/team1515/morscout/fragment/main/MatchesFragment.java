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
import org.team1515.morscout.activity.MatchActivity;
import org.team1515.morscout.adapter.MatchListAdapter;
import org.team1515.morscout.adapter.RecyclerItemClickListener;
import org.team1515.morscout.entity.Match;
import org.team1515.morscout.network.CookieRequest;
import org.team1515.morscout.network.NetworkUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.VISIBLE;

public class MatchesFragment extends EntityList {
    private List<Match> matches;

    private int matchesLength;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setType("match");

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
                String matchSearch = s.toString().toLowerCase();

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
                    ((MatchListAdapter) adapter).setMatches(searchedMatches);
                } else {
                    ((MatchListAdapter) adapter).setMatches(matches);
                }
                adapter.notifyDataSetChanged();
            }
        });

        setItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), MatchActivity.class);
                int matchNum = Integer.parseInt(((TextView) view.findViewById(R.id.matchlist_matchNumber)).getText().toString().split(" ")[1]);
                for (int i = 0; i < matches.size(); i++) {
                    if (matches.get(i).getNumber() == matchNum) {
                        intent.putExtra("match", new Gson().toJson(matches.get(i)));
                        startActivity(intent);
                        break;
                    }
                }

            }
        }));

        return view;
    }

    protected void initAdapter() {
        adapter = new MatchListAdapter();
    }

    @Override
    protected void getProgress() {
        Map<String, String> params = new HashMap<>();
        params.put("matchesLength", Integer.toString(matchesLength));

        CookieRequest requestProgress = new CookieRequest(Request.Method.POST,
                NetworkUtils.makeMorScoutURL("/getProgressForMatches", true),
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            preferences.edit().putString("matchProgress", response).apply();

                            List<Integer> progresses = new ArrayList<>();
                            JSONObject progressObj = new JSONObject(response);
                            for (int i = 0; i < matchesLength; i++) {
                                progresses.add(progressObj.getInt(Integer.toString(i + 1)));
                            }
                            ((MatchListAdapter) adapter).setProgresses(progresses);
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
    protected void processEntities(String matchStr) {
        matches = new ArrayList<>();

        try {
            JSONArray matchArray = new JSONArray(matchStr);

            // Store match length for getting progress
            matchesLength = matchArray.length();

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

                    matches.add(new Match(matchObject.getString("key"),
                            matchObject.getInt("match_number"),
                            matchObject.getString("comp_level"),
                            blueAlliance,
                            redAlliance,
                            matchObject.getLong("time"))
                    );
                }
            }

            sortMatches();
            ((MatchListAdapter) adapter).setMatches(matches);
            setListVisibility(VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sortMatches() {
        Collections.sort(matches, new Comparator<Match>() {
            @Override
            public int compare(Match lhs, Match rhs) {
                return lhs.getNumber() - rhs.getNumber();
            }
        });
    }
}
