package org.team1515.morscout.fragment.main;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.team1515.morscout.MorScout;
import org.team1515.morscout.R;
import org.team1515.morscout.activity.MatchActivity;
import org.team1515.morscout.adapter.MatchListAdapter;
import org.team1515.morscout.adapter.RecyclerItemClickListener;
import org.team1515.morscout.entity.Match;
import org.team1515.morscout.network.CookieRequest;
import org.team1515.morscout.network.NetworkUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.team1515.morscout.MorScout.preferences;

public class MatchesFragment extends EntityList {
    private ArrayList<Match> matches;
    private ArrayList<Match> currentlyDisplayedMatches;

    private boolean timeHasBeenSet;
    private int[] timeFilterStart = {0, 0};
    private int[] timeFilterEnd = {0, 0};
    private int[] dateFilter = {0, 0, 0};
    private String[] months = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};

    private int matchesLength;

    private Calendar cal;

    private ConstraintLayout filterLayout;

    private CheckBox timeCheckBox;
    private CheckBox dateCheckBox;

    private Button filterButton;
    private Button startTimeButton;
    private Button endTimeButton;
    private Button dateButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setType("match");
        View view = super.onCreateView(inflater, container);

        /* Begin populate UI variables */
        filterLayout = (ConstraintLayout) view.findViewById(R.id.filter_settings_layout);

        timeCheckBox = (CheckBox) view.findViewById(R.id.should_filter_time);
        dateCheckBox = (CheckBox) view.findViewById(R.id.should_filter_date);

        filterButton = (Button) view.findViewById(R.id.toggle_filter_btn);
        startTimeButton = (Button) view.findViewById(R.id.start_time_btn);
        endTimeButton = (Button) view.findViewById(R.id.end_time_btn);
        dateButton = (Button) view.findViewById(R.id.date_btn);
        /* End populate UI variables*/

        /* Begin initial value setting */
        cal = Calendar.getInstance();
        dateFilter[0] = cal.get(Calendar.YEAR);
        dateFilter[1] = cal.get(Calendar.MONTH);
        dateFilter[2] = cal.get(Calendar.DAY_OF_MONTH);

        Log.v("Stuff", String.format("[%s, %s, %s]", dateFilter[0], dateFilter[1], dateFilter[2]));

        dateButton.setText(String.format(Locale.US, "%s %d, %d", months[dateFilter[1]],
                dateFilter[2], dateFilter[0]));
        /* End initial value setting */


        /* Begin click listener creation */
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterLayout.getVisibility() == View.GONE)
                    filterLayout.setVisibility(View.VISIBLE);
                else
                    filterLayout.setVisibility(View.GONE);

                if (!timeHasBeenSet) {
                    String approxTime = String.format(Locale.US, "%d:%02d", cal.get(Calendar.HOUR_OF_DAY),
                            cal.get(Calendar.MINUTE) - (cal.get(Calendar.MINUTE) % 15));
                    startTimeButton.setText(approxTime);
                    endTimeButton.setText(approxTime);
                }
            }
        });

        timeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimeButton.setClickable(timeCheckBox.isChecked());
                endTimeButton.setClickable(timeCheckBox.isChecked());

                startTimeButton.setAlpha(timeCheckBox.isChecked() ? 1.0f : 0.5f);
                endTimeButton.setAlpha(timeCheckBox.isChecked() ? 1.0f : 0.5f);

                updateFilters();
            }
        });

        dateCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateButton.setClickable(dateCheckBox.isChecked());
                dateButton.setAlpha(dateCheckBox.isChecked() ? 1.0f : 0.5f);

                updateFilters();
            }
        });

        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePicker = new TimePickerDialog(getContext(), 0, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeFilterStart[0] = hourOfDay;
                        timeFilterStart[1] = minute;

                        startTimeButton.setText(String.format(Locale.US, "%d:%02d", hourOfDay, minute));
                        timeHasBeenSet = true;
                        updateFilters();
                    }
                }, 12, 15, true);

                timePicker.show();
            }
        });

        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePicker = new TimePickerDialog(getContext(), 0, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeFilterEnd[0] = hourOfDay;
                        timeFilterEnd[1] = minute;

                        endTimeButton.setText(String.format(Locale.US, "%d:%02d", hourOfDay, minute));
                        timeHasBeenSet = true;
                        updateFilters();
                    }
                }, 12, 15, true);

                timePicker.show();
            }
        });

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal = Calendar.getInstance();
                DatePickerDialog datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateFilter[0] = year;
                        dateFilter[1] = month;
                        dateFilter[2] = dayOfMonth;

                        dateButton.setText(String.format(Locale.US,"%s %d, %d", months[month], dayOfMonth, year));
                        updateFilters();
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

                datePicker.show();
            }
        });

        // For some reason, creating click listeners sets clickable to true, making this necessary
        startTimeButton.setClickable(false);
        endTimeButton.setClickable(false);
        dateButton.setClickable(false);
        /* End click listener creation */


        setSearchListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().toLowerCase();
                updateFilters();

                /* begin search block */
                if (!query.trim().isEmpty() && matches.size() > 0) {
                    List<Match> result = new ArrayList<>();

                    matchLoop:
                    for (Match match : currentlyDisplayedMatches) {
                        if (Integer.toString(match.getNumber()).contains(query)) {
                            result.add(match);
                            continue;
                        }

                        for (String team : match.getBlueAlliance()) {
                            if (team.contains(query)) {
                                result.add(match);
                                continue matchLoop;
                            }
                        }

                        for (String team : match.getRedAlliance()) {
                            if (team.contains(query)) {
                                result.add(match);
                                continue matchLoop;
                            }
                        }
                    }

                    ((MatchListAdapter) adapter).setMatches(result);
                } else {
                    ((MatchListAdapter) adapter).setMatches(currentlyDisplayedMatches);
                }
                adapter.notifyDataSetChanged();
            }
            /* end search block */
        });

        setItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), MatchActivity.class);
                int matchNum = Integer.parseInt(((TextView) view.findViewById(R.id.matchlist_matchNumber)).getText().toString().split(" ")[1]);
                for (int i = 0; i < matches.size(); i++) {
                    if (matches.get(i).getNumber() == matchNum) {
                        intent.putExtra("match", new Gson().toJson(matches.get(i)));
                        startActivity(intent);
                        break;
                    }
                }

            }
        }));

        return view;
    }

    protected void initAdapter() {
        adapter = new MatchListAdapter();
    }

    @Override
    protected void getProgress() {
        Map<String, String> params = new HashMap<>();
        params.put("matchesLength", Integer.toString(matchesLength));

        CookieRequest requestProgress = new CookieRequest(
                Request.Method.POST,
                NetworkUtils.makeMorScoutURL("/getProgressForMatches"),
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            preferences.edit().putString("matchProgress", response).commit();

                            List<Integer> progresses = new ArrayList<>();
                            JSONObject progressObj = new JSONObject(response);
                            for (int i = 0; i < matchesLength; i++) {
                                progresses.add(progressObj.getInt(Integer.toString(i + 1)));
                            }
                            ((MatchListAdapter) adapter).setProgresses(progresses);
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
        queue.add(requestProgress);
    }

    @Override
    protected void processEntities(String matchStr) {
        matches = new ArrayList<>();
        Log.v("MatchesFragment", matchStr);

        try {
            JSONArray matchArray = new JSONArray(matchStr);

            // Store match length for getting progress
            matchesLength = matchArray.length();

            for (int i = 0; i < matchArray.length(); i++) {
                JSONObject matchObject = matchArray.getJSONObject(i);

                //Add all qualifying matches
                if (matchObject.getString("comp_level").equals("qm")) {

                    JSONObject alliancesObject = matchObject.getJSONObject("alliances");

                    JSONArray blueArray = alliancesObject.getJSONObject("blue").getJSONArray("teams");
                    JSONArray redArray = alliancesObject.getJSONObject("red").getJSONArray("teams");

                    String[] blueAlliance = new String[3];
                    for (int j = 0; j < blueAlliance.length; j++) {
                        blueAlliance[j] = blueArray.getString(j).replaceAll("frc", "");
                    }

                    String[] redAlliance = new String[3];
                    for (int j = 0; j < redAlliance.length; j++) {
                        redAlliance[j] = redArray.getString(j).replaceAll("frc", "");
                    }

                    matches.add(new Match(matchObject.getString("key"),
                            matchObject.getInt("match_number"),
                            matchObject.getString("comp_level"),
                            blueAlliance,
                            redAlliance,
                            matchObject.getLong("time"))
                    );
                }
            }

            sortMatches();
//            startDate = new Date(matches.get(0).getTime());

            ((MatchListAdapter) adapter).setMatches(matches);
            setListVisibility(View.VISIBLE);
            updateFilters();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sortMatches() {
        Collections.sort(matches, new Comparator<Match>() {
            @Override
            public int compare(Match lhs, Match rhs) {
                return lhs.getNumber() - rhs.getNumber();
            }
        });
    }

    private void updateFilters() {
        boolean shouldFilterDate = dateCheckBox.isChecked();
        boolean shouldFilterTime = timeCheckBox.isChecked();

        if (shouldFilterDate || shouldFilterTime) {
            List<Match> matchesToFilter = matches;
            currentlyDisplayedMatches = new ArrayList<>();

            if (shouldFilterDate) {
                for (Match match : matchesToFilter) {
                    cal.setTimeInMillis(match.getTime());

                    if (cal.get(Calendar.YEAR) == dateFilter[0] && cal.get(Calendar.MONTH) == dateFilter[1] &&
                            cal.get(Calendar.DAY_OF_MONTH) == dateFilter[2]) {
                        currentlyDisplayedMatches.add(match);
                    }
                }
            }

            if (shouldFilterTime && timeHasBeenSet) {
                if (shouldFilterDate) {
                    matchesToFilter = (ArrayList<Match>) currentlyDisplayedMatches.clone();
                    currentlyDisplayedMatches = new ArrayList<>();
                }

                for (Match match : matchesToFilter) {
                    cal.setTimeInMillis(match.getTime());

                    int matchTimeMins = (cal.get(Calendar.HOUR_OF_DAY) * 60) + cal.get(Calendar.MINUTE);
                    int filterStartMins = (timeFilterStart[0] * 60) + timeFilterStart[1];
                    int filterEndMins = (timeFilterEnd[0] * 60) + timeFilterEnd[1];

                    if (filterStartMins <= matchTimeMins && matchTimeMins < filterEndMins) {
                        currentlyDisplayedMatches.add(match);
                    }
                }
            }
        } else {
            currentlyDisplayedMatches = matches;
        }

        ((MatchListAdapter) adapter).setMatches(currentlyDisplayedMatches);
        adapter.notifyDataSetChanged();
    }
}
