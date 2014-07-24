package com.cb.oneclipboard.server.logging;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * @author krishnaraj
 * <br/> taken from http://stackoverflow.com/a/7925828
 */
public class LevelBasedFileHandler extends FileHandler {
	
	private void setDefaults() {
		setFormatter(new SimpleFormatter());
	}
	
	public LevelBasedFileHandler(final Level level) throws IOException, SecurityException {
		super();
		super.setLevel(level);
		setDefaults();
	}

	public LevelBasedFileHandler(final String s, final Level level) throws IOException, SecurityException {
		super(s);
		super.setLevel(level);
		setDefaults();
	}

	public LevelBasedFileHandler(final String s, final boolean b, final Level level) throws IOException, SecurityException {
		super(s, b);
		super.setLevel(level);
		setDefaults();
	}

	public LevelBasedFileHandler(final String s, final int i, final int i1, final Level level) throws IOException, SecurityException {
		super(s, i, i1);
		super.setLevel(level);
		setDefaults();
	}

	public LevelBasedFileHandler(final String s, final int i, final int i1, final boolean b, final Level level) throws IOException, SecurityException {
		super(s, i, i1, b);
		super.setLevel(level);
		setDefaults();
	}

	// This is the important part that makes it work
	// it also breaks the contract in the JavaDoc for FileHandler.setLevel()
	@Override
	public void publish(final LogRecord logRecord) {
		if (logRecord.getLevel().equals(super.getLevel())) {
			super.publish(logRecord);
		}
	}
}