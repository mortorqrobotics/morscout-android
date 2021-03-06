package org.team1515.morscout.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.solovyev.android.views.llm.LinearLayoutManager;
import org.team1515.morscout.R;
import org.team1515.morscout.adapter.ReportListAdapter;
import org.team1515.morscout.entity.FormItem;
import org.team1515.morscout.network.CookieRequest;
import org.team1515.morscout.network.NetworkUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewReportFragment extends Fragment {
    private RequestQueue queue;
    private SharedPreferences preferences;

    private String context;
    private String requestPath;

    private RecyclerView yourReportList;
    private LinearLayoutManager yourReportManager;
    private ReportListAdapter yourReportAdapter;

    private RecyclerView otherReportList;
    private LinearLayoutManager otherReportManager;
    private ReportListAdapter otherReportAdapter;

    private List<List<FormItem>> yourReports;
    private List<List<FormItem>> otherReports;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewreport, container, false);

        context = getArguments().getString("context");
        if (context.equals("match")) {
            requestPath = "/getMatchReports";
        } else {
            requestPath = "/getTeamReports";
        }

        preferences = getActivity().getSharedPreferences(null, 0);
        queue = Volley.newRequestQueue(getContext());

        yourReportList = (RecyclerView) view.findViewById(R.id.viewreport_yourTeamReportsList);
        yourReportManager = new LinearLayoutManager(getContext());
        yourReportAdapter = new ReportListAdapter();
        yourReportList.setLayoutManager(yourReportManager);
        yourReportList.setAdapter(yourReportAdapter);

        otherReportList = (RecyclerView) view.findViewById(R.id.viewreport_otherTeamsReportsList);
        otherReportManager = new LinearLayoutManager(getContext());
        otherReportAdapter = new ReportListAdapter();
        otherReportList.setLayoutManager(otherReportManager);
        otherReportList.setAdapter(otherReportAdapter);

        getReports();

        return view;
    }

    public void getReports() {
        Map<String, String> params = new HashMap<>();
        if (context.equals("match")) {
            params.put("team", String.valueOf(getArguments().getInt("team")));
            params.put("match", String.valueOf(getArguments().getInt("match")));
        } else {
            params.put("teamNumber", String.valueOf(getArguments().getInt("team")));
            params.put("reportContext", "pit");
        }

        CookieRequest requestTeamReports = new CookieRequest(Request.Method.POST,
                NetworkUtils.makeMorScoutURL(requestPath),
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            List<FormItem> formItems = new ArrayList<>();

                            JSONObject matchObject = new JSONObject(response);
                            JSONArray yourTeamArray = matchObject.getJSONArray("yourTeam");
                            JSONArray otherTeamsArray = matchObject.getJSONArray("otherTeams");

                            if (yourTeamArray.length() > 0) {
                                JSONObject teamScouterInfo = yourTeamArray.getJSONObject(0).getJSONObject("scout");
                                FormItem teamScouterName = new FormItem(teamScouterInfo.getString("firstname") + " " + teamScouterInfo.getString("lastname"), "scouterInfo");
                                formItems.add(teamScouterName);
                            }

                            if (otherTeamsArray.length() > 0) {
                                JSONObject otherTeamScouterInfo = otherTeamsArray.getJSONObject(0).getJSONObject("scout");
                                FormItem otherTeamsScouterName = new FormItem(otherTeamScouterInfo.getString("firstname") + " " + otherTeamScouterInfo.getString("lastname"), "scouterInfo");
                                formItems.add(otherTeamsScouterName);
                            }

                            yourReports = new ArrayList<>();
                            otherReports = new ArrayList<>();

                            for (int i = 0; i < yourTeamArray.length(); i++) {
                                JSONArray formArray = yourTeamArray.getJSONObject(i).getJSONArray("data");

                                for (int j = 0; j < formArray.length(); j++) {
                                    JSONObject itemObj = formArray.getJSONObject(j);

                                    FormItem item;
                                    if (itemObj.has("value")) {
                                        String itemValue = itemObj.getString("value");

                                        if (itemValue.contains("&lt;")) {
                                            itemValue = itemValue.replaceFirst("&lt;", "<");
                                        }

                                        if (itemValue.contains("&gt;")) {
                                            itemValue = itemValue.replaceFirst("&gt;", ">");
                                        }

                                        item = new FormItem(itemObj.getString("name"), itemValue);
                                    } else {
                                        item = new FormItem(itemObj.getString("name"));
                                    }
                                    formItems.add(item);
                                }
                                yourReports.add(formItems);
                            }

                            for (int i = 0; i < otherTeamsArray.length(); i++) {
                                JSONArray formArray = otherTeamsArray.getJSONObject(i).getJSONArray("data");

                                for (int j = 0; j < formArray.length(); j++) {
                                    JSONObject itemObj = formArray.getJSONObject(j);

                                    FormItem item;
                                    if (itemObj.has("value")) {
                                        item = new FormItem(itemObj.getString("name"), itemObj.getString("value"));
                                    } else {
                                        item = new FormItem(itemObj.getString("name"));
                                    }
                                    formItems.add(item);
                                }
                                otherReports.add(formItems);
                            }

                            yourReportAdapter.setReports(yourReports);
                            otherReportAdapter.setReports(otherReports);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "MorScout could not connect to the server. Your report will be submitted as soon as a connection is available.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        queue.add(requestTeamReports);
    }
}
