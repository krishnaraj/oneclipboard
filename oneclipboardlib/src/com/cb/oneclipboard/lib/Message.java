package com.cb.oneclipboard.lib;

import java.io.Serializable;

public class Message implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7626096672509386693L;
	private String text;
	private MessageType messageType;
	
	public Message(String text, MessageType messageType) {
		super();
		this.text = text;
		this.messageType = messageType;
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
