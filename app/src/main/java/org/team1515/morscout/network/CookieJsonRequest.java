package org.team1515.morscout.network;

import org.json.JSONException;

import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.team1515.morscout.MorScout.preferences;

public class CookieJsonRequest extends JsonObjectRequest {


    public CookieJsonRequest(int method, String path, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, path, jsonRequest, listener, errorListener);
    }

    public CookieJsonRequest(int method, String path, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        this(method, path, new JSONObject(), listener, errorListener);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        //Store session-id cookie in storage
        if(response.headers.containsKey(NetworkUtils.SET_COOKIE_KEY) && response.headers.get(NetworkUtils.SET_COOKIE_KEY).startsWith(NetworkUtils.SESSION_COOKIE)) {
            String cookie = response.headers.get(NetworkUtils.SET_COOKIE_KEY);
            if (cookie.length() > 0) {
                String[] splitCookie = cookie.split(";");
                String[] splitSessionId = splitCookie[0].split("=");
                cookie = splitSessionId[1];
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(NetworkUtils.SESSION_COOKIE, cookie);
                editor.apply();
            }
        }

        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            if (!jsonString.isEmpty()) {
                return Response.success(new JSONObject(jsonString),
                        HttpHeaderParser.parseCacheHeaders(response));
            } else {
                return Response.success(new JSONObject(), HttpHeaderParser.parseCacheHeaders(response));
            }
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();
        if(headers == null || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<>();
        }

        //Insert session-id cookie into header
        String sessionId = preferences.getString(NetworkUtils.SESSION_COOKIE, "");

        if(sessionId.length() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(NetworkUtils.SESSION_COOKIE);
            builder.append("=");
            builder.append(sessionId);
            if(headers.containsKey(NetworkUtils.COOKIE_KEY)) {
                builder.append("; ");
                builder.append(headers.get(NetworkUtils.COOKIE_KEY));
            }
            headers.put(NetworkUtils.COOKIE_KEY, builder.toString());
        }

        return headers;
    }
}
