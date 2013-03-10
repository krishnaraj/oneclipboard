package com.cb.oneclipboard.lib;

public interface SocketListener {
	public void onMessageReceived(Message message);
	public void onConnect();
}
