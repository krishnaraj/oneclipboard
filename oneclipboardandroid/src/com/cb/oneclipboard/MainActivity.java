package com.cb.oneclipboard;

import java.io.InputStream;
import java.util.Properties;

import android.app.Activity;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.cb.oneclipboard.lib.ApplicationProperties;
import com.cb.oneclipboard.lib.Callback;
import com.cb.oneclipboard.lib.Message;
import com.cb.oneclipboard.lib.PropertyLoader;
import com.cb.oneclipboard.lib.SocketListener;
import com.cb.oneclipboard.lib.socket.ClipboardConnector;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
	private static final String[] PROP_LIST = { "app.properties" };

	private static String serverAddress = null;
	private static int serverPort;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		loadPropties(PROP_LIST);
		serverAddress = ApplicationProperties.getStringProperty("server");
		serverPort = ApplicationProperties.getIntProperty("server_port");

		TextView textView = (TextView) findViewById(R.id.text_view);

		final ClipboardManager clipBoard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		final ClipboardListener clipboardListener = new ClipboardListener(clipBoard, textView, new Callback() {

			@Override
			public void execute(Object object) {
				String clipboardText = (String) object;
				ClipboardConnector.send(new Message(clipboardText));
			}
		});
		clipBoard.addPrimaryClipChangedListener(clipboardListener);

		// Listen for clipboard content from other clients
		ClipboardConnector.connect(serverAddress, serverPort, new SocketListener() {

			@Override
			public void onMessageReceived(Message message) {
				clipboardListener.updateClipboardContent(message.getText());
				clipBoard.setText(message.getText());
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
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
