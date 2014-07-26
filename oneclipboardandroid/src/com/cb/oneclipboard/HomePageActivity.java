package com.cb.oneclipboard;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.widget.TextView;

public class HomePageActivity extends Activity {

	private static final String TAG = "HomePageActivity";

	private BroadcastReceiver receiver = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_page);

		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String clipboardText = intent.getStringExtra("message");
				TextView clipboardTextView = (TextView) findViewById(R.id.homePageText);
				clipboardTextView.setText(clipboardText);

			}
		};
	}

	@Override
	protected void onStart() {
		super.onStart();
		LocalBroadcastManager.getInstance(this).registerReceiver((receiver), new IntentFilter(ClipboardApplication.CLIPBOARD_UPDATED));
		
		// This is necessary as the textView won't be updated when the activity wasn't visible.
		final ClipboardManager clipBoard = (ClipboardManager) getSystemService( CLIPBOARD_SERVICE );
		TextView clipboardTextView = (TextView)findViewById( R.id.homePageText );
		clipboardTextView.setText( clipBoard.getText() );
	}

	@Override
	protected void onStop() {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_home_page, menu);
		return true;
	}

}
