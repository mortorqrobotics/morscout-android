package org.team1515.morscout.fragment.main;

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

import org.team1515.morscout.R;
import org.team1515.morscout.network.CookieRequest;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {
    private RequestQueue queue;
    private SharedPreferences preferences;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        queue = Volley.newRequestQueue(getContext());
        preferences = getContext().getSharedPreferences(null, 0);

        getProfile();

        return view;
    }

    private void getProfile() {
        Map<String, String> params = new HashMap<>();
//        params.put("scoutID", preferences.getString("userId", ""));
        params.put("scoutID", "56500985d218a24520d4ac9a");

        CookieRequest profileRequest = new CookieRequest(Request.Method.POST,
                "/showTasks",
                preferences,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        queue.add(profileRequest);
    }
}
