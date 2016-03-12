package org.team1515.morscout.fragment.match;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.team1515.morscout.R;
import org.team1515.morscout.network.CookieRequest;

import java.util.HashMap;
import java.util.Map;

public class StrategyFragment extends Fragment {

    private SharedPreferences preferences;
    private RequestQueue queue;

    String strategy;

    TextView strategyText;
    Button submitStrategy;

    AlertDialog.Builder strategyBuilder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_strategy, container, false);

        preferences = getActivity().getSharedPreferences(null, 0);
        queue = Volley.newRequestQueue(getContext());

        strategyText = (TextView) view.findViewById(R.id.strategy_text);

        getStrategy();

        submitStrategy = (Button) view.findViewById(R.id.submit_strategy);
        submitStrategy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setStrategy();}});

        return view;
    }

    private void getStrategy() {
        Map<String, String> params = new HashMap<>();
        params.put("match", String.valueOf(getArguments().getInt("match")));

        CookieRequest strategyRequest = new CookieRequest(Request.Method.POST,
                "/getMatchStrategy",
                params,
                preferences,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject strategyObj = new JSONObject(response);

                            strategyText.setText(strategyObj.getString("strategy"));
                        } catch (JSONException e) {
                            strategyText.setText("No strategies yet");
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        queue.add(strategyRequest);
    }

    public void setStrategy() {
        strategyBuilder = new AlertDialog.Builder(getContext());
        strategyBuilder.setView(R.layout.set_strategy);
        strategyBuilder.setPositiveButton();
        strategyBuilder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Map<String, String> params = new HashMap<>();
                params.put("match", String.valueOf(getArguments().getInt("match")));
                params.put("strategy", "Do this");

                CookieRequest submitStrategy = new CookieRequest(Request.Method.POST,
                        "/setMatchStrategy",
                        params,
                        preferences,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject strategyObj = new JSONObject(response);

                                    strategyText.setText(strategyObj.getString("strategy"));
                                } catch (JSONException e) {
                                    strategyText.setText("No strategies yet");
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                );
                queue.add(submitStrategy);
                getStrategy();
            }
        });
        strategyBuilder.setNegativeButton("Cancel", null);
    }
}
