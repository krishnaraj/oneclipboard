package com.cb.oneclipboard.lib;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultPropertyLoader implements PropertyLoader {
	private final static Logger LOGGER = Logger.getLogger(DefaultPropertyLoader.class.getName());

	@Override
	public void load(Properties properties, String fileName) {
		try {
			InputStream fileStream = DefaultPropertyLoader.class.getClassLoader().getResourceAsStream(fileName);
			properties.load(fileStream);
			fileStream.close();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Exception when loading properties", e);
		}

	}

}
