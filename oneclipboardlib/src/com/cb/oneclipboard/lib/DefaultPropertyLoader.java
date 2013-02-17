package com.cb.oneclipboard.lib;

import java.io.InputStream;
import java.util.Properties;

public class DefaultPropertyLoader implements PropertyLoader {

	@Override
	public void load(Properties properties, String fileName) {
		try {
			InputStream fileStream = DefaultPropertyLoader.class.getClassLoader().getResourceAsStream(fileName);
			properties.load(fileStream);
			fileStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
