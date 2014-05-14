package com.cb.oneclipboard.desktop;

import com.cb.oneclipboard.lib.ApplicationProperties;

public class Utilities {

	public static String getFullApplicationName() {
		return ApplicationProperties.getStringProperty("app_name") + " " + ApplicationProperties.getStringProperty("version");
	}
}
