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
import android.widget.LinearLayout;
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

    RecyclerView matchesList;
    MatchesAdapter listAdapter;
    LinearLayoutManager matchLayoutManager;

    List<TextView> dataViews;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matches, container, false);

        preferences = getActivity().getSharedPreferences(null, 0);
        queue = Volley.newRequestQueue(getContext());

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
            }
        });

        matchesList = (RecyclerView) view.findViewById(R.id.matchlist);

        listAdapter = new MatchesAdapter();

        matchLayoutManager = new LinearLayoutManager(getContext());
        matchesList.setLayoutManager(matchLayoutManager);

        matchesList.setAdapter(listAdapter);

        return view;
    }

    public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.ViewHolder> {
        private List<Match> matches;

        public MatchesAdapter() {
            matches = new ArrayList<>();
            getMatches();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LinearLayout layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_match, parent, false);
            ViewHolder viewHolder = new ViewHolder(layout);
            return viewHolder;
        }

        public void getMatches() {
            CookieRequest getMatchesRequest = new CookieRequest(Request.Method.POST, "/getMatchesForCurrentRegional", preferences, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject match = jsonArray.getJSONObject(i);
                            matches.add(new Match(match.getString("key")));
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
            queue.add(getMatchesRequest);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return matches.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public LinearLayout layout;

            public ViewHolder(LinearLayout layout) {
                super(layout);
                this.layout = layout;
            }
        }
    }
}
