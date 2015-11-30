package com.cb.oneclipboard.util;

import com.cb.oneclipboard.lib.socket.ClipboardConnector;

public class Utility {
	
	public static String getConnectionStatus() {
		if( ClipboardConnector.isConnected() ) {
			return "Connected to " + ClipboardConnector.getServerName();
		} else {
			return "Not connected.";
		}
	}
}
