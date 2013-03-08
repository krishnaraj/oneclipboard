package com.cb.oneclipboard.lib;

import java.io.Serializable;

public class Message implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7626096672509386693L;
	private String text;
	
	public Message(String text) {
		super();
		this.text = text;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
