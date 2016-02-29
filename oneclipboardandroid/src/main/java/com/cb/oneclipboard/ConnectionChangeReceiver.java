package com.cb.oneclipboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectionChangeReceiver extends BroadcastReceiver {
	private static final String TAG = ConnectionChangeReceiver.class.getName();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		ClipboardApplication app = (ClipboardApplication) context.getApplicationContext();
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		
		if ( activeNetInfo != null && activeNetInfo.isConnected() && !app.isConnected() ) {
			Log.d(TAG, "Connected to network.");
			app.establishConnection();
		} else {
			Log.d(TAG, "Network connection unavailable");
			app.updateNotification();
		}
		
	}
}