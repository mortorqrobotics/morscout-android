package org.team1515.morscout.fragment.main;

import android.app.DownloadManager;
import android.content.SharedPreferences;
import android.graphics.Color;
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

    EditText feedbackText;

    Button submitFeedback;

    boolean isEmpty;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        preferences = getActivity().getSharedPreferences(null, 0);
        queue = Volley.newRequestQueue(getContext());

        feedbackText = (EditText) view.findViewById(R.id.feedback_text);

        submitFeedback = (Button) view.findViewById(R.id.feedback_submit);

        isEmpty = false;

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setFeedbackListener();
    }

    public void setFeedbackListener() {
        submitFeedback.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                sendFeedback();
            }
        });
    }

    public void sendFeedback() {
        String content = feedbackText.getText().toString();

        if (content.trim().isEmpty()) {
            feedbackText.setText("");
            feedbackText.setHintTextColor(Color.RED);
            isEmpty = true;
        }

        if (isEmpty) {
            Toast.makeText(getContext(), "Please fill out your form completely.", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("content", content);

        CookieRequest sendFeedback = new CookieRequest(Request.Method.POST, "/sendFeedback", params, preferences, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                    feedbackText.setText("");
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
}
