package com.cb.oneclipboard.lib;

import java.util.Properties;

public class ApplicationProperties {

	static Properties properties = new Properties();
	
	public static void loadProperties(String[] fileList, PropertyLoader loader){
		for (int i = fileList.length - 1; i >= 0; i--) {
			loader.load(properties, fileList[i]);
		}
	}
	
	public static String getStringProperty(String key){
		return properties.getProperty(key);
	}
	
	public static int getIntProperty(String key) throws Exception{
		return Integer.parseInt(getStringProperty(key));
	}
	

}
