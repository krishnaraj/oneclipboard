package com.cb.oneclipboard;

import android.content.ClipboardManager;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
import android.util.Log;
import android.widget.TextView;

import com.cb.oneclipboard.lib.ApplicationProperties;
import com.cb.oneclipboard.lib.MessageSender;

public class ClipboardListener implements OnPrimaryClipChangedListener {
	private static final String TAG = "MainActivity";
	
	TextView textView = null;
	ClipboardManager clipBoard = null;
	
	public ClipboardListener(ClipboardManager clipBoard, TextView textView) {
		this.clipBoard = clipBoard;
		this.textView = textView;
	}

	@Override
	public void onPrimaryClipChanged() {
		CharSequence clipText = clipBoard.getText();
		textView.setText(clipText);
		
		if(clipText != null){
			try{
				MessageSender.send(
						ApplicationProperties.getStringProperty("server"), 
						ApplicationProperties.getIntProperty("port"),
						clipText.toString());
			}catch(Exception e){
				Log.e(TAG, e.getMessage());
			}
		}
	}
}
