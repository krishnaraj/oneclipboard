package com.cb.oneclipboard.lib;

import java.io.Serializable;

public class Message implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7626096672509386693L;
	private String text;
	private MessageType messageType;
	private User user;
	
	public Message(String text, User user) {
		this(text, MessageType.CLIPBOARD_TEXT, user);
	}
	public Message(String text, MessageType messageType, User user) {
		super();
		this.text = text;
		this.messageType = messageType;
		this.user = user;
	}
	public MessageType getMessageType() {
		return messageType;
	}
	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
