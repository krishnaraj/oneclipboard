package com.cb.oneclipboard;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cb.oneclipboard.lib.User;
import com.cb.oneclipboard.util.IntentUtil;

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getName();

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		if ( !OneClipboardService.isRunning ) {
			setContentView( R.layout.login );
			Button loginButton = (Button) findViewById( R.id.btnLogin );
			loginButton.setOnClickListener( new View.OnClickListener() {

				@Override
				public void onClick( View arg0 ) {
					TextView usernameField = (TextView) findViewById( R.id.usernameField );
					TextView passwordField = (TextView) findViewById( R.id.passwordField );
					String username = usernameField.getText().toString().trim();
					String password = passwordField.getText().toString().trim();

					if ( username.length() > 0 && password.length() > 0 ) {
						User user = new User( username, password );
						( (ClipboardApplication) getApplicationContext() ).setUser( user );

						Intent oneclipboardServiceIntent = new Intent( MainActivity.this, OneClipboardService.class );
						startService( oneclipboardServiceIntent );

						finish();
					}
				}

			} );
		} else {
			startActivity( IntentUtil.getHomePageIntent( this ) );
			finish();
		}
	}
}
