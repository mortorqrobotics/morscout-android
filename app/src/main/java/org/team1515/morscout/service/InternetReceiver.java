package org.team1515.morscout.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.support.v4.content.LocalBroadcastManager;

public class InternetReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Check for events other than network stuffs
        if (!intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION) &&
        !intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION) &&
        !intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            return;
        }

        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connManager == null) {
            return;
        }

        //Check if connected
        if(connManager.getActiveNetworkInfo() != null && connManager.getActiveNetworkInfo().isConnected()) {
            //Push intent to activities
            Intent connectionIntent = new Intent("connection");
            LocalBroadcastManager.getInstance(context).sendBroadcast(connectionIntent);
        }
    }
}
