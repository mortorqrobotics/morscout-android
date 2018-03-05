package org.team1515.morscout.network;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.android.volley.NetworkResponse;

import java.util.Map;

public class NetworkUtils {
    // Constants
    public static final String HOSTMT = "https://www.morteam.com";
    public static final String HOSTMS = "https://www.scout.morteam.com";
    public static final String PATH_PREFIX = "/api";
    public static final int PORT = 443;

    public static final String SET_COOKIE_KEY = "set-cookie";
    public static final String COOKIE_KEY = "Cookie";
    public static final String SESSION_COOKIE = "connect.sid";

    public static String makeMorTeamURL(String path, boolean usePrefix) {
        return HOSTMT + ":" + PORT + (usePrefix ? PATH_PREFIX : "") + path;
    }

    public static String makeMorScoutURL(String path) {
        return HOSTMS + path;
    }

    public static String makePictureURL(String path, String size) {
        if (path.length() > 2 && path.substring(0, 3).equals("/pp")) {
            return "https://s3-us-west-2.amazonaws.com/profilepics.morteam.com/" + path.substring(3) + size;
        } else {
            return "https://www.morteam.com" + path + size;
        }
    }

    public static void catchNetworkError(Context context, NetworkResponse response, Map<String, Pair<String, String>> errors) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setPositiveButton("Okay", null);

        if (response != null) {
            if (response.statusCode == 400) {
                String message = new String(response.data);

                boolean setDialog = false;
                for (String errorMessage : errors.keySet()) {
                    if (message.equals(errorMessage)) {
                        Pair<String, String> error = errors.get(errorMessage);
                        dialog.setTitle(error.first);
                        dialog.setTitle(error.second);
                        setDialog = true;
                        break;
                    }
                }

                if (!setDialog) {
                    dialog.setTitle("An unknown error has occurred");
                    dialog.setMessage("Please try again later or contact the developers for assistance.");
                }

            } else {
                dialog.setTitle("Cannot connect to server");
                dialog.setMessage("Please make sure you have a stable internet connection.");

                Log.e("FUCK", "FUCK");
            }
        } else {
            dialog.setTitle("Cannot connect to server");
            dialog.setMessage("Please make sure you have a stable internet connection.");

            Log.e("FUCK", "SHIT");
        }

        dialog.show();
    }
}