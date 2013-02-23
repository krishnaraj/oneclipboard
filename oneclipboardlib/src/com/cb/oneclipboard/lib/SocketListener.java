package com.cb.oneclipboard.lib;

public interface SocketListener {
	public void onMessageReceived(String ip, Message message);
	public void onPortReady(int replyPort);
}
