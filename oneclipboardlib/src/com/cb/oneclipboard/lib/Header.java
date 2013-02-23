package com.cb.oneclipboard.lib;

public class Header {
	private String serverAddress;
	private int serverPort;
	private int replyPort;
	
	public Header(String serverAddress, int serverPort, int replyPort) {
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		this.replyPort = replyPort;
	}
	
	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public int getReplyPort() {
		return replyPort;
	}

	public void setReplyPort(int replyPort) {
		this.replyPort = replyPort;
	}
}
