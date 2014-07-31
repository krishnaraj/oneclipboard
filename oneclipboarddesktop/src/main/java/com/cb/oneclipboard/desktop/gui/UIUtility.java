package com.cb.oneclipboard.desktop.gui;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

public class UIUtility {
	
	protected static Image createImage(String path, String description) {
		URL imageURL = Tray.class.getResource(path);

		if (imageURL == null) {
			System.err.println("Resource not found: " + path);
			return null;
		} else {
			return (new ImageIcon(imageURL, description)).getImage();
		}
	}
}
