package com.cb.oneclipboard;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
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

                // Explicitly setting height as otherwise the textView doesn't fill screen height for long text.
                LayoutParams params = clipboardTextView.getLayoutParams();
                params.height = getResources().getDimensionPixelSize(R.dimen.text_view_height);
                clipboardTextView.setLayoutParams(params);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver), new IntentFilter(ClipboardApplication.CLIPBOARD_UPDATED));

        // This is necessary as the textView won't be updated when the activity wasn't visible.
        final ClipboardManager clipBoard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        TextView clipboardTextView = (TextView) findViewById(R.id.homePageText);
        clipboardTextView.setText(clipBoard.getText());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ClipboardApplication app = (ClipboardApplication) getApplication();

        switch (item.getItemId()) {
            case R.id.menu_quit:
                stopService();
                finish();
                return true;
            case R.id.menu_logout:
                stopService();
                app.pref.clear();
                Intent login = new Intent(this, MainActivity.class);
                startActivity(login);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void stopService() {
        Intent oneclipboardServiceIntent = new Intent(this, OneClipboardService.class);
        stopService(oneclipboardServiceIntent);
    }
}
