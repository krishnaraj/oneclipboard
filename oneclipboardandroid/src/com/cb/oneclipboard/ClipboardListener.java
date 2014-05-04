package com.cb.oneclipboard;

import android.content.ClipboardManager;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
import android.util.Log;

import com.cb.oneclipboard.lib.Callback;

public class ClipboardListener implements OnPrimaryClipChangedListener {
	private static final String TAG = "ClipboardListener";
	
	ClipboardManager clipBoard = null;
	Callback callback = null;
	
	private String clipboardContent = null;
	
	public ClipboardListener(ClipboardManager clipBoard, Callback callback) {
		this.clipBoard = clipBoard;
		this.callback = callback;
	}

	@Override
	public void onPrimaryClipChanged() {
		CharSequence clipText = clipBoard.getText();
		
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
