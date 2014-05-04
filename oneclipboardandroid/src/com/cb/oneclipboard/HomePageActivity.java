package com.cb.oneclipboard;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class HomePageActivity extends Activity {

	private static final String TAG = "HomePageActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_page);
	}
	
	@Override
	protected void onStart() {
		final ClipboardManager clipBoard = (ClipboardManager) getSystemService( CLIPBOARD_SERVICE );
		TextView clipboardTextView = (TextView)findViewById( R.id.homePageTextView );
		clipboardTextView.setText( clipBoard.getText() );
		super.onStart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_home_page, menu);
		return true;
	}

}
