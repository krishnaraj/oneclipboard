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
import com.cb.oneclipboard.lib.MessageSender;
import com.cb.oneclipboard.lib.PropertyLoader;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
	private static final String[] PROP_LIST = { "app.properties" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		loadPropties(PROP_LIST);

		TextView textView = (TextView) findViewById(R.id.text_view);

		ClipboardManager clipBoard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		clipBoard.addPrimaryClipChangedListener(new ClipboardListener(clipBoard, textView));
		
		register();
	}

	private void register() {
		try{
			MessageSender.register(
					ApplicationProperties.getStringProperty("server"), 
					ApplicationProperties.getIntProperty("port"));
		}catch(Exception e){
			Log.e(TAG, e.getMessage(), e);
		}
		
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
