package org.team1515.morscout.fragment.match;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.team1515.morscout.R;
import org.team1515.morscout.network.CookieRequest;

import java.util.HashMap;
import java.util.Map;

public class MatchScoutFragment extends Fragment {
    private SharedPreferences preferences;
    private RequestQueue queue;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matchscout, container, false);

        preferences = getActivity().getSharedPreferences(null, 0);
        queue = Volley.newRequestQueue(getContext());

        requestScoutForm();

        return view;
    }

    public void requestScoutForm() {
        Map<String, String> params = new HashMap<>();
        params.put("context", "match");

        CookieRequest requestForm = new CookieRequest(Request.Method.POST, "/getScoutForm", params, preferences, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.print(response);
                try {
                    JSONArray scoutArray = new JSONArray(response);
                    for (int i = 0; i < scoutArray.length(); i++) {
                        JSONObject object = scoutArray.getJSONObject(i);
                        System.out.println(object.getString("type"));
                        //All different types: dropdown, radio, button
                        if (object.getString("type").equalsIgnoreCase("dropdown")) {
                            System.out.println("dropdown");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error");
            }
        });
        queue.add(requestForm);
    }
}
