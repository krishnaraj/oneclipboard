package com.cb.oneclipboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.cb.oneclipboard.lib.CipherManager;
import com.cb.oneclipboard.lib.User;
import com.cb.oneclipboard.util.IntentUtil;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ClipboardApplication app = ((ClipboardApplication) getApplicationContext());

        if (!OneClipboardService.isRunning) {
            if (app.pref.getUsername() != null && app.pref.getPassword() != null) {
                User user = new User(app.pref.getUsername(), app.pref.getPassword());
                initAndStart(user);
            } else {
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
                            User user = new User(username, password);
                            app.pref.setUsername(username);
                            app.pref.setPassword(password);
                            initAndStart(user);
                        }
                    }

                });
            }

            app.loadProperties();
        } else {
            startActivity(IntentUtil.getHomePageIntent(this));
            finish();
        }
    }

    public void initAndStart(User user) {
        ClipboardApplication app = ((ClipboardApplication) getApplicationContext());
        app.setUser(user);
        app.setCipherManager(new CipherManager(user));

        Intent oneclipboardServiceIntent = new Intent(MainActivity.this, OneClipboardService.class);
        startService(oneclipboardServiceIntent);

        Toast.makeText(this, getString(R.string.app_name) + " has started!", Toast.LENGTH_SHORT).show();

        finish();
    }
}
