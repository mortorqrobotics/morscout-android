package org.team1515.morscout.fragment.main;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import org.team1515.morscout.network.CookieRequest;

/**
 * Created by MorTorq_Business on 5/13/2016.
 */
public abstract class EntityList extends Fragment{
    protected String requestType;
    private String requestString;
    private RequestQueue queue;
    private SharedPreferences preferences;
    private ProgressBar progressBar;
    private RecyclerView viewsList;
    protected Response.Listener<String> responseListener;

    JSONObject progressObj;

    public View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_matches, container, false);

        preferences = getActivity().getSharedPreferences(null, 0);
        queue = Volley.newRequestQueue(getContext());

        initViews(view);

        getEntities();

        return view;
    }

    public abstract void initViews(View view);

    public void getProgress() {
        if (requestType.equalsIgnoreCase("pit")) {
            requestString = "/getProgressForPit";
        } else {
            requestString = "/getProgressForMatches";
        }

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

    public void getEntities() {
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
