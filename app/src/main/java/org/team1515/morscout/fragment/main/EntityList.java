package org.team1515.morscout.fragment.main;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.team1515.morscout.R;
import org.team1515.morscout.adapter.RecyclerItemClickListener;
import org.team1515.morscout.network.CookieRequest;
import org.team1515.morscout.network.NetworkUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static org.team1515.morscout.MorScout.preferences;

public abstract class EntityList extends Fragment {

    protected RequestQueue queue;

    // Base layout items
    private SwipeRefreshLayout refreshLayout;
    private EditText searchBar;
    private ProgressBar progressBar;
    private RecyclerView viewsList;
    private LinearLayoutManager layoutManager;
    protected RecyclerView.Adapter adapter;

    // Type specific
    protected String requestType;
    private String requestString;

    public View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_entitylist, container, false);

        queue = Volley.newRequestQueue(getContext());

        initViews(view);
        getEntities();

        return view;
    }

    protected abstract void initAdapter();

    private void initViews(View view) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.entitylist_refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        refreshLayout.setRefreshing(false);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEntities();
            }
        });

        searchBar = (EditText) view.findViewById(R.id.entitylist_searchBar);

        progressBar = (ProgressBar) view.findViewById(R.id.entitylist_loading);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.rgb(255, 197, 71), android.graphics.PorterDuff.Mode.MULTIPLY);

        viewsList = (RecyclerView) view.findViewById(R.id.entitylist_list);
        viewsList.setVisibility(View.GONE);
        layoutManager = new LinearLayoutManager(getContext());
        viewsList.setLayoutManager(layoutManager);

        initAdapter();
        viewsList.setAdapter(adapter);
    }

    protected void setSearchListener(TextWatcher listener) {
        searchBar.addTextChangedListener(listener);
    }

    protected void setItemTouchListener(RecyclerItemClickListener listener) {
        viewsList.addOnItemTouchListener(listener);
    }

    protected void setType(String type) {
        this.requestType = type;
        if (requestType.equals("pit")) {
            requestString = "/getProgressForPit";
        } else if (requestType.equals("match")) {
            requestString = "/getProgressForMatches";
        }
    }

    protected abstract void getProgress();

    private void getEntities() {
        if (requestType.equalsIgnoreCase("pit")) {
            requestString = "/getTeamListForRegional";
            requestType = "teams";
        } else if (requestType.equalsIgnoreCase("match")) {
            requestString = "/getMatchesForCurrentRegional";
            requestType = "matches";
        }

        CookieRequest requestEntities = new CookieRequest(Request.Method.POST,
                NetworkUtils.makeMorScoutURL(requestString, true),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        preferences.edit().putString(requestType, response).apply();
                        processEntities(response);
                        getProgress();

                        refreshLayout.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        queue.add(requestEntities);
    }

    protected void setListVisibility(int visibility) {
        if (visibility == VISIBLE) {
            viewsList.setVisibility(VISIBLE);
            progressBar.setVisibility(GONE);
        } else if (visibility == GONE) {
            viewsList.setVisibility(GONE);
            progressBar.setVisibility(VISIBLE);
        }
    }

    protected abstract void processEntities(String matches);
}
