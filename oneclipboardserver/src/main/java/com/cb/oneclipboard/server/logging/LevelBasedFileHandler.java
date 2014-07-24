package com.cb.oneclipboard.server.logging;

import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * @author krishnaraj
 * <br/> ...based on http://stackoverflow.com/a/7925828
 */
public class LevelBasedFileHandler extends FileHandler {
	List<Level> levels = null;
	
	private void setDefaults() {
		setFormatter(new SimpleFormatter());
	}
	
	public LevelBasedFileHandler(final String s, List<Level> levels) throws IOException, SecurityException {
		super(s);
		this.levels = levels;
		setDefaults();
	}

	// This is the important part that makes it work
	// it also breaks the contract in the JavaDoc for FileHandler.setLevel()
	@Override
	public void publish(final LogRecord logRecord) {
		if (levels.contains(logRecord.getLevel())) {
			super.publish(logRecord);
		}
	}
}