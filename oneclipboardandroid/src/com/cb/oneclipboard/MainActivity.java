package com.cb.oneclipboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		Button loginButton = (Button) findViewById(R.id.btnLogin);
		loginButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				TextView usernameField = (TextView) findViewById(R.id.usernameField);
				TextView passwordField = (TextView) findViewById(R.id.passwordField);
				String username = usernameField.getText().toString().trim();
				String password = passwordField.getText().toString().trim();

				if (username.length() > 0 && password.length() > 0) {
					Bundle bundle = new Bundle();
					bundle.putString("username", username);
					bundle.putString("password", password);

					Intent i = new Intent(MainActivity.this, HomePageActivity.class);
					i.putExtras(bundle);

					startActivity(i);
				}
			}

		});

	}
}
