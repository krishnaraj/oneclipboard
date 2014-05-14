package com.cb.oneclipboard.desktop;

import com.cb.oneclipboard.lib.Callback;

public class ClipboardPollTask implements Runnable{
	private String clipboardContent = null;
	TextTransfer textTransfer;
	Callback callback;
	
	public ClipboardPollTask(TextTransfer textTransfer, Callback callback){
		this.callback = callback;
		this.textTransfer = textTransfer;
	}
	
	@Override
	public void run() {
		String content = textTransfer.getClipboardContents();
		if(content != null && !content.equals(clipboardContent)){
			clipboardContent = content;
			callback.execute(content);
		}
	}
	
	public void updateClipboardContent(String newContent){
		this.clipboardContent = newContent;
	}
}
