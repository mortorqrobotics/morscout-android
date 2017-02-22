package org.team1515.morscout.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.team1515.morscout.R;
import org.team1515.morscout.network.CookieRequest;
import org.team1515.morscout.network.NetworkUtils;

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

        // Set up action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Exo2-Medium.ttf");
        toolbarTitle.setTypeface(typeface);

        toolbar.setVisibility(View.GONE);
    }

    public void register(View view) {
        EditText firstNameView = (EditText) findViewById(R.id.register_firstName);
        EditText lastNameView = (EditText) findViewById(R.id.register_lastName);
        EditText usernameView = (EditText) findViewById(R.id.register_username);
        EditText passwordView = (EditText) findViewById(R.id.register_password);
        EditText passwordConfirmView = (EditText) findViewById(R.id.register_passwordConfirm);
        EditText emailView = (EditText) findViewById(R.id.register_email);
        EditText phoneView = (EditText) findViewById(R.id.register_phone);
        String firstName = firstNameView.getText().toString();
        String lastName = lastNameView.getText().toString();
        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();
        String passwordConfirm = passwordConfirmView.getText().toString();
        String email = emailView.getText().toString();
        String phone = phoneView.getText().toString();

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
        if (username.trim().isEmpty()) {
            usernameView.setText("");
            usernameView.setHintTextColor(Color.RED);
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
        if (email.trim().isEmpty()) {
            emailView.setText("");
            emailView.setHintTextColor(Color.RED);
            isEmpty = true;
        }
        if (phone.trim().isEmpty()) {
            phoneView.setText("");
            phoneView.setHintTextColor(Color.RED);
            isEmpty = true;
        }

        if (isEmpty) {
            isEmpty = false;
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("firstname", firstName);
        params.put("lastname", lastName);
        params.put("username", username);
        params.put("password", password);
        params.put("password_confirm", passwordConfirm);
        params.put("email", email);
        params.put("phone", phone);

        CookieRequest registerRequest = new CookieRequest(Request.Method.POST,
                NetworkUtils.makeMorTeamURL("/createUser", true),
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        if(response.equals("success")) {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (response.equals("exists")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setTitle("One or more of the inputted items already exists.");
                            builder.setPositiveButton("Okay", null);
                            builder.create().show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setTitle("One or more of the inputted items is invalid.");
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
    }
}
