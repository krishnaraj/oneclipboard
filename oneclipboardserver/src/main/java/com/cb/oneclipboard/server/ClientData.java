package com.cb.oneclipboard.server;

public class ClientData {
	private String ip;
	private int port;
	
	public ClientData(String ip, int port) {
		super();
		this.ip = ip;
		this.port = port;
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
}
