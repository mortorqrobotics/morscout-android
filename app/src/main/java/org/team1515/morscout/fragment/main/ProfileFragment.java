package org.team1515.morscout.fragment.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.team1515.morscout.R;
import org.team1515.morscout.network.CacheRequestQueue;
import org.team1515.morscout.network.CookieRequest;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {
    private RequestQueue queue;
    private SharedPreferences preferences;
    private ImageLoader imageLoader;

    NetworkImageView picture;
    TextView matchReports;
    TextView pitReports;
    TextView uniqueTeams;
    TextView matchesComplete;
    TextView matchesIncomplete;
    TextView matchCompletion;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        queue = Volley.newRequestQueue(getContext());
        preferences = getContext().getSharedPreferences(null, 0);

        picture = (NetworkImageView) view.findViewById(R.id.profile_picture);
        matchReports = (TextView) view.findViewById(R.id.profile_matchReports);
        pitReports = (TextView) view.findViewById(R.id.profile_pitReports);
        uniqueTeams = (TextView) view.findViewById(R.id.profile_uniqueTeams);
        matchesComplete = (TextView) view.findViewById(R.id.profile_matchesComplete);
        matchesIncomplete = (TextView) view.findViewById(R.id.profile_matchesIncomplete);
        matchCompletion = (TextView) view.findViewById(R.id.profile_matchCompletion);

        getProfile();

        return view;
    }

    private void getProfile() {
        imageLoader = CacheRequestQueue.getInstance(getContext()).getImageLoader();
        final String url = "http://www.morteam.com" + preferences.getString("picPath", "") + "-300";
        System.out.println(url);
        imageLoader.get(url, ImageLoader.getImageListener(picture,
                android.R.drawable.ic_dialog_alert, android.R.drawable
                        .ic_dialog_alert));
        picture.setImageUrl(url, imageLoader);

        Map<String, String> params = new HashMap<>();
//        params.put("scoutID", preferences.getString("userId", ""));
        params.put("userID", "56500985d218a24520d4ac9a");
        params.put("scoutID", "56500985d218a24520d4ac9a");

        CookieRequest statsRequest = new CookieRequest(Request.Method.POST,
                "/getUserStats",
                params,
                preferences,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject statsObj = new JSONObject(response);
                            matchReports.setText(statsObj.getString("matchesScouted"));
                            pitReports.setText(statsObj.getString("pitsScouted"));
                            uniqueTeams.setText(statsObj.getString("teamsScouted"));
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

        CookieRequest tasksRequest = new CookieRequest(Request.Method.POST,
                "/showTasks",
                params,
                preferences,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject tasksObj = new JSONObject(response);

                            JSONArray incompleteMatches = tasksObj.getJSONArray("matchesNotDone");
                            String incompleteMatchesStr = "";
                            for(int i = 0; i < incompleteMatches.length(); i++) {
                                incompleteMatchesStr += incompleteMatches.getString(i);
                                if(i < incompleteMatches.length() - 1) {
                                    incompleteMatchesStr += ", ";
                                }
                            }
                            matchesIncomplete.setText(incompleteMatchesStr);

                            JSONArray completeMatches = tasksObj.getJSONArray("matchesDone");
                            String completeMatchesStr = "";
                            for(int i = 0; i < completeMatches.length(); i++) {
                                completeMatchesStr += completeMatches.getString(i);
                                if(i < completeMatches.length() - 1) {
                                    completeMatchesStr += ", ";
                                }
                            }
                            matchesComplete.setText(completeMatchesStr);

                            matchCompletion.setText(completeMatches.length() + "/" + (incompleteMatches.length() + completeMatches.length()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        queue.add(statsRequest);
        queue.add(tasksRequest);
    }
}
