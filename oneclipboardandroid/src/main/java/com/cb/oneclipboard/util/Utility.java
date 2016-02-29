package com.cb.oneclipboard.util;

import com.cb.oneclipboard.lib.socket.ClipboardConnector;

public class Utility {
	
	public static String getConnectionStatus(ClipboardConnector clipboardConnector) {
		if( clipboardConnector.isConnected() ) {
			return "Connected to " + clipboardConnector.getServerName();
		} else {
			return "Not connected.";
		}
	}
}
