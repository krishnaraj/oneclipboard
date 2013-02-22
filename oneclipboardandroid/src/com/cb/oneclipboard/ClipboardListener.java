package com.cb.oneclipboard;

import android.content.ClipboardManager;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
import android.util.Log;
import android.widget.TextView;

import com.cb.oneclipboard.lib.ApplicationProperties;
import com.cb.oneclipboard.lib.Callback;
import com.cb.oneclipboard.lib.MessageSender;

public class ClipboardListener implements OnPrimaryClipChangedListener {
	private static final String TAG = "MainActivity";
	
	TextView textView = null;
	ClipboardManager clipBoard = null;
	Callback callback = null;
	
	private String clipboardContent = null;
	
	public ClipboardListener(ClipboardManager clipBoard, TextView textView, Callback callback) {
		this.clipBoard = clipBoard;
		this.textView = textView;
		this.callback = callback;
	}

	@Override
	public void onPrimaryClipChanged() {
		CharSequence clipText = clipBoard.getText();
		textView.setText(clipText);
		
		if(clipText != null){
			try{
				String content = clipText.toString();
				if(content !=null && !content.equals(clipboardContent)){
					clipboardContent = content;
					callback.execute(clipText.toString());
				}
			}catch(Exception e){
				Log.e(TAG, e.getMessage());
			}
		}
	}
	
	public void updateClipboardContent(String newContent){
		this.clipboardContent = newContent;
	}
}
