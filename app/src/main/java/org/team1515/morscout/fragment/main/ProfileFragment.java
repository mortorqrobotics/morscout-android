package org.team1515.morscout.fragment.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.team1515.morscout.R;
import org.team1515.morscout.network.CookieRequest;
import org.team1515.morscout.network.NetworkUtils;

import java.util.HashMap;
import java.util.Map;

import static org.team1515.morscout.MorScout.preferences;
import static org.team1515.morscout.MorScout.queue;

public class ProfileFragment extends Fragment {
//    private ImageLoader imageLoader;

    private ImageView picture;
    private TextView matchReports;
    private TextView pitReports;
    private TextView uniqueTeams;
    private TextView matchesComplete;
    private TextView matchesIncomplete;
    private TextView matchCompletion;
    private TextView assignments;
    private CheckBox scoutCaptain;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        picture = (ImageView) view.findViewById(R.id.profile_picture);
        matchReports = (TextView) view.findViewById(R.id.profile_matchReports);
        pitReports = (TextView) view.findViewById(R.id.profile_pitReports);
        uniqueTeams = (TextView) view.findViewById(R.id.profile_uniqueTeams);
        matchesComplete = (TextView) view.findViewById(R.id.profile_matchesComplete);
        matchesIncomplete = (TextView) view.findViewById(R.id.profile_matchesIncomplete);
        matchCompletion = (TextView) view.findViewById(R.id.profile_matchCompletion);
        assignments = (TextView) view.findViewById(R.id.profile_assignments);
        scoutCaptain = (CheckBox) view.findViewById(R.id.profile_captain);

        getProfile();

        return view;
    }

    private void getProfile() {
//        imageLoader = CacheRequestQueue.getInstance(getContext()).getImageLoader();
//        final String url = "http://www.morteam.com" + preferences.getString("picPath", "") + "-300";
//        System.out.println(url);
//        imageLoader.get(url, ImageLoader.getImageListener(picture,
//                android.R.drawable.ic_dialog_alert, android.R.drawable
//                        .ic_dialog_alert));
//        picture.setImageUrl(url, imageLoader);
//        ImageCookieRequest pictureRequest = new ImageCookieRequest("http://www.morteam.com" + preferences.getString("picPath", "") + "-300",
//                preferences,
//                new Response.Listener<Bitmap>() {
//                    @Override
//                    public void onResponse(Bitmap response) {
//                        picture.setImageBitmap(response);
//                    }
//                }, 0, 0, null, Bitmap.Config.RGB_565, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//        queue.add(pictureRequest);

        Map<String, String> params = new HashMap<>();
        params.put("scoutID", preferences.getString("userId", ""));

        CookieRequest statsRequest = new CookieRequest(
                Request.Method.POST,
                NetworkUtils.makeMorScoutURL("/getUserStats"),
                params,
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

        CookieRequest tasksRequest = new CookieRequest(
                Request.Method.POST,
                NetworkUtils.makeMorScoutURL("/showTasks"),
                params,
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


                            JSONArray assignmentArray = tasksObj.getJSONArray("assignments");
                            if(assignmentArray.length() == 0) {
                                assignments.setText("None");
                            } else {
                                SpannableStringBuilder assignmentBuilder = new SpannableStringBuilder();
                                for (int i = 0; i < assignmentArray.length(); i++) {
                                    JSONObject assignmentObj = assignmentArray.getJSONObject(i);
                                    SpannableString assignmentMatches = new SpannableString("From Match "
                                            + assignmentObj.getString("startMatch") + " to " + assignmentObj.getString("endMatch")
                                            + ": ");
                                    assignmentMatches.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, assignmentMatches.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    SpannableString assignmentTeams = new SpannableString(assignmentObj.getString("alliance").substring(0, 1).toUpperCase() + assignmentObj.getString("alliance").substring(1)
                                            + " Alliance, Team " + assignmentObj.getString("teamSection"));

                                    assignmentBuilder.append(assignmentMatches);
                                    assignmentBuilder.append(assignmentTeams);
                                    assignmentBuilder.append("\n\n");
                                }
                                assignments.setText(assignmentBuilder);
                            }
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
