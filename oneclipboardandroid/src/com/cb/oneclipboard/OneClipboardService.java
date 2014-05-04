package com.cb.oneclipboard;

import java.io.InputStream;
import java.util.Properties;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.cb.oneclipboard.lib.ApplicationProperties;
import com.cb.oneclipboard.lib.Callback;
import com.cb.oneclipboard.lib.Message;
import com.cb.oneclipboard.lib.MessageType;
import com.cb.oneclipboard.lib.PropertyLoader;
import com.cb.oneclipboard.lib.SocketListener;
import com.cb.oneclipboard.lib.User;
import com.cb.oneclipboard.lib.socket.ClipboardConnector;
import com.cb.oneclipboard.util.IntentUtil;

public class OneClipboardService extends Service {
	private static final String TAG = "OneClipboardService";

	private static final String[] PROP_LIST = {
		"app.properties"
	};

	private static String serverAddress = null;
	private static int serverPort;
	private static User user = null;
	public static volatile boolean isRunning = false;

	@Override
	public int onStartCommand( Intent intent, int flags, int startId ) {
		Log.d( TAG, "onStartCommand called" );
		initAndStart();
		
		PendingIntent pendingIntent = PendingIntent.getActivity( this, 0, IntentUtil.getHomePageIntent( this ), PendingIntent.FLAG_CANCEL_CURRENT );

		Notification n = new Notification.Builder( this ).setContentTitle( "Oneclipboard" ).setContentText( "Your clipboard text will appear here.." )
				.setSmallIcon( R.drawable.logo ).setContentIntent( pendingIntent ).setAutoCancel( true ).build();
		
		startForeground( 1, n );
		
		isRunning = true;
		
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

	public void initAndStart() {
		init();
		start();
	}

	@Override
	public IBinder onBind( Intent arg0 ) {
		// TODO Auto-generated method stub
		return null;
	}

	public void init() {
		loadPropties( PROP_LIST );
		serverAddress = ApplicationProperties.getStringProperty( "server" );
		serverPort = ApplicationProperties.getIntProperty( "server_port" );

		// Set the user
		user = ( (ClipboardApplication) getApplicationContext() ).getUser();
	}

	public void start() {
		final ClipboardManager clipBoard = (ClipboardManager) getSystemService( CLIPBOARD_SERVICE );
		final ClipboardListener clipboardListener = new ClipboardListener( clipBoard, new Callback() {

			@Override
			public void execute( Object object ) {
				String clipboardText = (String) object;
				ClipboardConnector.send( new Message( clipboardText, user ) );
			}
		} );
		clipBoard.addPrimaryClipChangedListener( clipboardListener );

		// Listen for clipboard content from other clients
		ClipboardConnector.connect( serverAddress, serverPort, user, new SocketListener() {

			@Override
			public void onMessageReceived( Message message ) {
				clipboardListener.updateClipboardContent( message.getText() );
				clipBoard.setText( message.getText() );
			}

			@Override
			public void onConnect() {
				ClipboardConnector.send( new Message( "register", MessageType.REGISTER, user ) );
			}

		} );
	}

	private void loadPropties( String[] fileList ) {
		PropertyLoader loader = new PropertyLoader() {

			@Override
			public void load( Properties properties, String fileName ) {
				try {
					InputStream fileStream = getAssets().open( fileName );
					properties.load( fileStream );
					fileStream.close();
				} catch ( Exception e ) {
					Log.d( TAG, e.getMessage() );
				}
			}
		};
		ApplicationProperties.loadProperties( fileList, loader );
	}

}
