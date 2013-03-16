package com.cb.oneclipboard;

import java.io.InputStream;
import java.util.Properties;

import com.cb.oneclipboard.lib.ApplicationProperties;
import com.cb.oneclipboard.lib.Callback;
import com.cb.oneclipboard.lib.Message;
import com.cb.oneclipboard.lib.MessageType;
import com.cb.oneclipboard.lib.PropertyLoader;
import com.cb.oneclipboard.lib.SocketListener;
import com.cb.oneclipboard.lib.User;
import com.cb.oneclipboard.lib.socket.ClipboardConnector;

import android.os.Bundle;
import android.app.Activity;
import android.content.ClipboardManager;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class HomePageActivity extends Activity {

	private static final String TAG = "HomePageActivity";
	private static final String[] PROP_LIST = { "app.properties" };

	private static String serverAddress = null;
	private static int serverPort;
	private static User user = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_page);
		
		init();
		start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_home_page, menu);
		return true;
	}

	public void init() {
		loadPropties(PROP_LIST);
		serverAddress = ApplicationProperties.getStringProperty("server");
		serverPort = ApplicationProperties.getIntProperty("server_port");

		// Set the user
		Bundle bundle = getIntent().getExtras();
		user = new User(bundle.getString("username"), bundle.getString("password"));
	}

	public void start() {
		TextView textView = (TextView) findViewById(R.id.homePageTextView);
		final ClipboardManager clipBoard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		final ClipboardListener clipboardListener = new ClipboardListener(clipBoard, textView, new Callback() {

			@Override
			public void execute(Object object) {
				String clipboardText = (String) object;
				ClipboardConnector.send(new Message(clipboardText, user));
			}
		});
		clipBoard.addPrimaryClipChangedListener(clipboardListener);

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
			}

		});
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
					Log.d(TAG, e.getMessage());
				}
			}
		};
		ApplicationProperties.loadProperties(fileList, loader);
	}

}
