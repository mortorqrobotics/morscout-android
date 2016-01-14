package org.team1515.morscout.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import org.team1515.morscout.network.CookieRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences preferences;
    RequestQueue queue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences(null, 0);
        queue = Volley.newRequestQueue(this);
    }

    public void login(View view) {
        TextView usernameView = (TextView) findViewById(R.id.login_username);
        TextView passwordView = (TextView) findViewById(R.id.login_password);
        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();

        if(username.trim().isEmpty()) {
            usernameView.setText("");
            usernameView.setHintTextColor(getResources().getColor(R.color.red));
            return;
        }
        if(password.isEmpty()) {
            passwordView.setHintTextColor(getResources().getColor(R.color.red));
            return;
        }


        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        CookieRequest loginRequest = new CookieRequest(Request.Method.POST,
                "/login",
                preferences,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject userObject = new JSONObject(response);
                            preferences.edit()
                                    .putString("username", userObject.getString("username"))
                                    .putString("firstName", userObject.getString("firstName"))
                                    .putString("lastName", userObject.getString("lastName"))
                                    .putString("teamCode", userObject.getString("teamCode"))
                                    .putInt("teamNumber", userObject.getInt("teamNumber"))
                                    .putString("teamName", userObject.getString("teamName"))
                                    .putBoolean("admin", userObject.getBoolean("admin"))
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
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
