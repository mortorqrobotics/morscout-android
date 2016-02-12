package org.team1515.morscout.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import org.team1515.morscout.entities.Team;
import org.team1515.morscout.network.CookieRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prozwood on 1/25/16.
 */
public class TeamListFragment extends Fragment {
    private RequestQueue queue;
    private SharedPreferences preferences;

    EditText searchTeams;
    String teamSearch;

    RecyclerView teamsList;
    TeamListAdapter listAdapter;
    LinearLayoutManager teamListLayoutManager;

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
                teamSearch = searchTeams.getText().toString();
            }
        });

        teamsList = (RecyclerView) view.findViewById(R.id.teamlist);

        listAdapter = new TeamListAdapter();

        teamListLayoutManager = new LinearLayoutManager(getContext());
        teamsList.setLayoutManager(teamListLayoutManager);

        teamsList.setAdapter(listAdapter);

        return view;
    }

    public class TeamListAdapter extends RecyclerView.Adapter<TeamListAdapter.ViewHolder> {
        private List<Team> teams;

        public TeamListAdapter() {
            teams = new ArrayList<>();
            getTeams();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.teamlist_list, parent, false);
            ViewHolder viewHolder = new ViewHolder(relativeLayout);
            return viewHolder;
        }

        public void getTeams() {
            CookieRequest requestTeams = new CookieRequest(Request.Method.POST, "/getTeamListForRegional", preferences, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject team = jsonArray.getJSONObject(i);
                            teams.add(new Team(team.getString("key")));
                        }
                        notifyDataSetChanged();
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

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final TextView team = (TextView) holder.relativeLayout.findViewById(R.id.teamlist_team);

            CardView cardView = (CardView) holder.relativeLayout.findViewById(R.id.teamlist_cardview);

            View.OnClickListener dateClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    team.setText("Hi");
                }
            };

            cardView.setOnClickListener(dateClickListener);
            team.setOnClickListener(dateClickListener);
        }

        @Override
        public int getItemCount() {
            return teams.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public RelativeLayout relativeLayout;

            public ViewHolder(RelativeLayout relativeLayout) {
                super(relativeLayout);
                this.relativeLayout = relativeLayout;
            }

        }

        public void addTeam(Team newTeam) {
            teams.add(newTeam);
            notifyDataSetChanged();
        }
    }
}
