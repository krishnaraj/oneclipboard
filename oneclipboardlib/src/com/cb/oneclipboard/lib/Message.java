package com.cb.oneclipboard.lib;

public class Message {
	private String hostAddress;
	private String text;
	private MessageType messageType;
	
	public Message(String hostAddress, String text, MessageType messageType) {
		super();
		this.hostAddress = hostAddress;
		this.text = text;
		this.messageType = messageType;
	}
	public String getHostAddress() {
		return hostAddress;
	}
	public void setHostAddress(String hostAddress) {
		this.hostAddress = hostAddress;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public MessageType getMessageType() {
		return messageType;
	}
	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}
}
