package org.team1515.morscout.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.team1515.morscout.R;
import org.team1515.morscout.fragment.HomeFragment;
import org.team1515.morscout.network.CookieRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences preferences;
    RequestQueue queue;

    boolean isEmpty = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences(null, 0);
        queue = Volley.newRequestQueue(this);

        if(preferences.contains("username")
                && preferences.contains("firstName")
                && preferences.contains("lastName")
                && preferences.contains("teamCode")
                && preferences.contains("teamNumber")
                && preferences.contains("teamName")
                && preferences.contains("admin")) {

        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void login(View view) {
        TextView usernameView = (TextView) findViewById(R.id.login_username);
        TextView passwordView = (TextView) findViewById(R.id.login_password);
        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();

        if(username.trim().isEmpty()) {
            usernameView.setText("");
            usernameView.setHintTextColor(Color.RED);
            isEmpty = true;
        }
        if(password.trim().isEmpty()) {
            passwordView.setText("");
            passwordView.setHintTextColor(Color.RED);
            isEmpty = true;
        }

        if (isEmpty) {
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        CookieRequest loginRequest = new CookieRequest(Request.Method.POST,
                "/login",
                params,
                preferences,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject userObject = new JSONObject(response);
                            preferences.edit()
                                    .putString("_id", userObject.getString("_id"))
                                    .putString("username", userObject.getString("username"))
                                    .putString("firstName", userObject.getString("firstName"))
                                    .putString("lastName", userObject.getString("lastName"))
                                    .putBoolean("admin", userObject.getBoolean("admin"))
                                    .putString("teamCode", userObject.getString("teamCode"))
                                    .putInt("teamNumber", userObject.getInt("teamNumber"))
                                    .putString("teamName", userObject.getString("teamName"))
                                    .apply();
                        } catch(JSONException e) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("Login Error");
                            builder.setMessage("You entered an incorrect username or password.");
                            builder.setPositiveButton("Okay", null);
                            builder.create().show();
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
        queue.add(loginRequest);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
