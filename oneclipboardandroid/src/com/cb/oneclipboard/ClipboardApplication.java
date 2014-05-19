package com.cb.oneclipboard;

import java.io.InputStream;
import java.util.Properties;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
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
import com.cb.oneclipboard.util.Utility;

public class ClipboardApplication extends Application {
	public static final int NOTIFICATION_ID = 1;

	private static final String TAG = ClipboardApplication.class.getName();

	private static final String[] PROP_LIST = { "app.properties" };

	private User user = null;
	private NotificationCompat.Builder notificationBuilder = null;

	private static String serverAddress = null;
	private static int serverPort;

	public void loadPropties() {
		loadPropties(PROP_LIST);
		serverAddress = ApplicationProperties.getStringProperty("server");
		serverPort = ApplicationProperties.getIntProperty("server_port");
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void establishConnection() {
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
		ClipboardConnector.connect(serverAddress, serverPort, user, new SocketListener() {

			@Override
			public void onMessageReceived(Message message) {
				clipboardListener.updateClipboardContent(message.getText());
				clipBoard.setText(message.getText());
			}

			@Override
			public void onConnect() {
				ClipboardConnector.send(new Message("register", MessageType.REGISTER, user));
				updateNotification();
			}

			@Override
			public void onDisconnect() {
				Log.d(TAG, "disconnected!");
				updateNotification();
			}

		});
	}
	
	public NotificationCompat.Builder getNotificationBuilder( Context context ) {
		PendingIntent pendingIntent = PendingIntent.getActivity( context, 0, IntentUtil.getHomePageIntent( context ), PendingIntent.FLAG_CANCEL_CURRENT );

		notificationBuilder = new NotificationCompat.Builder( context )
			.setContentTitle( "Oneclipboard" )
			.setContentText( Utility.getConnectionStatus() )
			.setSmallIcon( R.drawable.logo )
			.setContentIntent( pendingIntent )
			.setAutoCancel( true );
		
		return notificationBuilder;
	}
	
	public void updateNotification() {
		if( notificationBuilder != null ) {
			notificationBuilder.setContentText( Utility.getConnectionStatus() );
			NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.notify(ClipboardApplication.NOTIFICATION_ID, notificationBuilder.build());
		}
	}

	private void loadPropties(String[] fileList) {
		PropertyLoader loader = new PropertyLoader() {

			@Override
			public void load(Properties properties, String fileName) {
				try {
					InputStream fileStream = getAssets().open(fileName);
					properties.load(fileStream);
					fileStream.close();
				} catch (Exception e) {
					e.printStackTrace();
					Log.d(TAG, e.getMessage());
				}
			}
		};
		ApplicationProperties.loadProperties(fileList, loader);
	}
}
