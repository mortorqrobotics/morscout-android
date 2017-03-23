package org.team1515.morscout.fragment.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.team1515.morscout.R;
import org.team1515.morscout.network.CookieRequest;
import org.team1515.morscout.network.NetworkUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.team1515.morscout.MorScout.preferences;
import static org.team1515.morscout.MorScout.queue;

public class SettingsFragment extends Fragment {
    Spinner regionalYears;
    Spinner regionalsList;

    ArrayAdapter<String> yearsAdapter;
    ArrayAdapter<String> regionalsAdapter;

    List<String> yearsArray;
    List<String> regionalsArray;
    List<String> eventCodes;

    String year;
    String currentRegional;

    int position;

    Button setRegionalButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        setRegionalButton = (Button) view.findViewById(R.id.settings_setRegional);

        yearsArray = new ArrayList<>();
        for (int i = 1992; i < 2018; i++) {
            yearsArray.add(i + "");
        }

        yearsAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_item, yearsArray);
        yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        regionalYears = (Spinner) view.findViewById(R.id.settings_year);
        regionalYears.setAdapter(yearsAdapter);

        regionalsArray = new ArrayList<>();

        regionalsAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_item, regionalsArray);
        regionalsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        regionalsList = (Spinner) view.findViewById(R.id.settings_regionalsList);
        regionalsList.setAdapter(regionalsAdapter);

        eventCodes = new ArrayList<>();

        boolean returnValue = preferences.getBoolean("returnValue", false);

        if (returnValue) {
            return view;
        }
        return null;
    }

    @Override
    public void onStart() {
        super.onStart();
        setRegionalListener();
        setYearListener();
    }

    public void setRegionalListener() {
        setRegionalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = regionalsList.getSelectedItemPosition();
                if (position > -1 && position < eventCodes.size()) {
                    currentRegional = eventCodes.get(position);
                    setRegional();
                }
            }
        });
    }

    public void setYearListener() {
        regionalYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                year = regionalYears.getSelectedItem().toString();
                getRegionals();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    public void getRegionals() {
        Map<String, String> params = new HashMap<>();
        params.put("year", year);

        CookieRequest requestRegionals = new CookieRequest(Request.Method.POST, NetworkUtils.makeMorScoutURL("/getRegionalsForTeam"), params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    regionalsArray.clear();
                    eventCodes.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject regional = jsonArray.getJSONObject(i);
                        regionalsArray.add(regional.getString("name"));
                        eventCodes.add(regional.getString("key"));
                    }
                    regionalsAdapter.notifyDataSetChanged();
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
    }

    public void setRegional() {
        Map<String, String> params = new HashMap<>();
        params.put("eventCode", currentRegional);
        params.put("year", year);

        CookieRequest setRegionalRequest = new CookieRequest(Request.Method.POST, NetworkUtils.makeMorScoutURL("/chooseCurrentRegional"), params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Toast.makeText(getContext(), "Regional has been set to the " + regionalsArray.get(position) + "!", Toast.LENGTH_SHORT).show();
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
        queue.add(setRegionalRequest);
    }
}
