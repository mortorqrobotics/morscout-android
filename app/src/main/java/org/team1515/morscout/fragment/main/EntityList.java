package org.team1515.morscout.fragment.main;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.team1515.morscout.R;
import org.team1515.morscout.adapter.RecyclerItemClickListener;
import org.team1515.morscout.entity.Entity;
import org.team1515.morscout.network.CookieRequest;

import java.util.ArrayList;
import java.util.List;

public abstract class EntityList extends Fragment {

    private RequestQueue queue;
    private SharedPreferences preferences;

    // Base layout items
    private EditText searchBar;
    private ProgressBar progressBar;
    private RecyclerView viewsList;
    private LinearLayoutManager layoutManager;
    protected RecyclerView.Adapter adapter;
    private Response.Listener<String> responseListener;
    private List<Entity> entities;

    // Type specific
    protected String requestType;
    private String requestString;
    JSONObject progressObj;

    public View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_entitylist, container, false);

        preferences = getActivity().getSharedPreferences(null, 0);
        queue = Volley.newRequestQueue(getContext());

        initViews(view);

        entities = new ArrayList<>();

        return view;
    }

    protected abstract void initAdapter();

    private void initViews(View view) {
        searchBar = (EditText) view.findViewById(R.id.entitylist_searchBar);

        progressBar = (ProgressBar) view.findViewById(R.id.entitylist_loading);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.rgb(255, 197, 71), android.graphics.PorterDuff.Mode.MULTIPLY);

        viewsList = (RecyclerView) view.findViewById(R.id.entitylist_list);
        viewsList.setVisibility(View.GONE);
        layoutManager = new LinearLayoutManager(getContext());
        viewsList.setLayoutManager(layoutManager);

        initAdapter();
    }

    protected void setSearchListener(TextWatcher listener) {
        searchBar.addTextChangedListener(listener);
    }

    protected  void setItemTouchListener(RecyclerItemClickListener listener) {
        viewsList.addOnItemTouchListener(listener);
    }

    protected void setType(String type) {
        this.requestType = type;
        if (requestType.equals("pit")) {
            requestString = "/getProgressForPit";
        } else if (requestType.equals("match")){
            requestString = "/getProgressForMatches";
        }
    }

    private void getProgress() {

        CookieRequest requestProgress = new CookieRequest(Request.Method.POST, requestString, preferences, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    preferences.edit().putString(requestType.toLowerCase() + "Progress", response).apply();
                    progressObj = new JSONObject(response);
                    getEntities();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    progressObj = new JSONObject(preferences.getString(requestType.toLowerCase() + "Progress", "[]"));
                    getEntities();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "An error has occurred. Please try again later.", Toast.LENGTH_SHORT).show();
                }
                error.printStackTrace();
            }
        });
        queue.add(requestProgress);
    }

    private void getEntities() {
        if (requestType.equalsIgnoreCase("pit")) {
            requestString = "/getTeamListForRegional";
            requestType = "teams";
        } else {
            requestString = "/getMatchesForCurrentRegional";
            requestType = "matches";
        }

        CookieRequest requestTeams = new CookieRequest(Request.Method.POST, requestString, preferences, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                preferences.edit().putString(requestType, response).apply();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(requestTeams);
    }
}
