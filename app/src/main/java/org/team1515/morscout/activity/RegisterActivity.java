package org.team1515.morscout.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

public class RegisterActivity extends AppCompatActivity {
    SharedPreferences preferences;
    RequestQueue queue;

    boolean isEmpty = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        preferences = getSharedPreferences(null, 0);
        queue = Volley.newRequestQueue(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void register(View view) {
        TextView firstNameView = (TextView) findViewById(R.id.register_firstName);
        TextView lastNameView = (TextView) findViewById(R.id.register_lastName);
        TextView teamCodeView = (TextView) findViewById(R.id.register_teamCode);
        TextView passwordView = (TextView) findViewById(R.id.register_password);
        TextView passwordConfirmView = (TextView) findViewById(R.id.register_passwordConfirm);
        String firstName = firstNameView.getText().toString();
        String lastName = lastNameView.getText().toString();
        String teamCode = teamCodeView.getText().toString();
        String password = passwordView.getText().toString();
        String passwordConfirm = passwordConfirmView.getText().toString();

        if (firstName.trim().isEmpty()) {
            firstNameView.setText("");
            firstNameView.setHintTextColor(Color.RED);
            isEmpty = true;
        }
        if (lastName.trim().isEmpty()) {
            lastNameView.setText("");
            lastNameView.setHintTextColor(Color.RED);
            isEmpty = true;
        }
        if (teamCode.trim().isEmpty()) {
            teamCodeView.setText("");
            teamCodeView.setHintTextColor(Color.RED);
            isEmpty = true;
        }
        if (password.trim().isEmpty()) {
            passwordView.setText("");
            passwordView.setHintTextColor(Color.RED);
            isEmpty = true;
        }
        if (passwordConfirm.trim().isEmpty()) {
            passwordConfirmView.setText("");
            passwordConfirmView.setHintTextColor(Color.RED);
            isEmpty = true;
        }

        if (isEmpty) {
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("firstName", firstName);
        params.put("lastName", lastName);
        params.put("teamCode", teamCode);
        params.put("password", password);
        params.put("passwordConfirm", passwordConfirm);

        CookieRequest registerRequest = new CookieRequest(Request.Method.POST,
                "/signup",
                params,
                preferences,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")) {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            try {
                                finalize();
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setTitle("One or more of the inputted information is incorrect");
                            builder.setPositiveButton("Okay", null);
                            builder.create().show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(registerRequest);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
