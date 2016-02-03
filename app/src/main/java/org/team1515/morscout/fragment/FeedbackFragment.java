package org.team1515.morscout.fragment;

import android.app.DownloadManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.team1515.morscout.R;
import org.team1515.morscout.network.CookieRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by prozwood on 1/25/16.
 */
public class FeedbackFragment extends Fragment {
    private RequestQueue queue;
    private SharedPreferences preferences;

    EditText teamNumberText;
    EditText feedbackText;

    Button submitFeedback;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        preferences = getActivity().getSharedPreferences(null, 0);
        queue = Volley.newRequestQueue(getContext());

        teamNumberText = (EditText) view.findViewById(R.id.feedback_teamNumber);
        feedbackText = (EditText) view.findViewById(R.id.feedback_text);

        submitFeedback = (Button) view.findViewById(R.id.feedback_submit);
        submitFeedback.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Map<String, String> params = new HashMap<>();
                params.put("teamNumber", teamNumberText.getText().toString());
                params.put("content", feedbackText.getText().toString());

                CookieRequest sendFeedback = new CookieRequest(Request.Method.POST, "/sendFeedback", params, preferences, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                            feedbackText.setText("");
                            teamNumberText.setText("");
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
                queue.add(sendFeedback);
            }
        });

        return view;
    }
}
