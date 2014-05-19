package com.cb.oneclipboard;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class OneClipboardService extends Service {
	private static final String TAG = "OneClipboardService";

	public static volatile boolean isRunning = false;
	
	@Override
	public int onStartCommand( Intent intent, int flags, int startId ) {
		Log.d( TAG, "onStartCommand called" );
		
		NotificationCompat.Builder notificationBuilder = ( (ClipboardApplication) getApplicationContext() ).getNotificationBuilder(this);
		startForeground( ClipboardApplication.NOTIFICATION_ID, notificationBuilder.build() );
		
		isRunning = true;
		
		start();
		
		return Service.START_STICKY;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		stopForeground( true );
		isRunning = false;
		super.onDestroy();
	}

	@Override
	public IBinder onBind( Intent arg0 ) {
		// TODO Auto-generated method stub
		return null;
	}

	public void start() {
		( (ClipboardApplication) getApplicationContext() ).initializeClipboardListener();
		( (ClipboardApplication) getApplicationContext() ).establishConnection();
	}
}
