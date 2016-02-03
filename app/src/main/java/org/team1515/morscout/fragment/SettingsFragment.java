package org.team1515.morscout.fragment;

import android.app.DownloadManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import org.team1515.morscout.activity.MainActivity;
import org.team1515.morscout.network.CookieRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by prozwood on 1/25/16.
 */
public class SettingsFragment extends Fragment {
    private RequestQueue queue;
    private SharedPreferences preferences;

    TextView regionalYear;
    String year;

    List<String> spinnerArray;
    Spinner regionalsList;

    Button setRegional;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        setRegional = (Button) view.findViewById(R.id.settings_setRegional);

        preferences = getActivity().getSharedPreferences(null, 0);
        queue = Volley.newRequestQueue(getContext());

        regionalYear = (TextView) view.findViewById(R.id.settings_year);
        regionalYear.addTextChangedListener(new TextWatcher() {

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
                year = regionalYear.getText().toString();
            }
        });

        spinnerArray = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionalsList = (Spinner) view.findViewById(R.id.settings_regionalsList);
        regionalsList.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getRegionals();
    }

    // SERVER OFFLINE 2/2/16
    // CODE WORKING

    public void getRegionals() {
        setRegional.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Map<String, String> params = new HashMap<>();
                params.put("year", year);

                CookieRequest requestRegionals = new CookieRequest(Request.Method.POST, "/getRegionalsForTeam", params, preferences, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "An error has occurred. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(requestRegionals);

                regionalsList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                        String selected = regionalsList.getSelectedItem().toString();
//                        if (selected.equals("sand")) {
//                            Toast.makeText(getActivity().getApplicationContext(), "SAND", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(getActivity().getApplicationContext(), "WICH", Toast.LENGTH_SHORT).show();
//                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }

                });
            }
        });
    }
}
