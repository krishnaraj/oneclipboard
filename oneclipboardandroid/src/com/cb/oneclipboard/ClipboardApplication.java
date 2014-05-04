package com.cb.oneclipboard;

import android.app.Application;

import com.cb.oneclipboard.lib.User;

public class ClipboardApplication extends Application {
	private User user = null;

	public User getUser() {
		return user;
	}

	public void setUser( User user ) {
		this.user = user;
	}

}
