package com.cb.oneclipboard.desktop.gui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ApplicationUI {
	
	public static void show(){

		/* Use an appropriate Look and Feel */
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			// UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// Schedule a job for the event-dispatching thread:
		// adding TrayIcon.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
	
	private static void createAndShowGUI() {
		// Show the login dialog
		LoginDialog loginDialog = new LoginDialog();
		loginDialog.setVisible(true);
		// System tray
		Tray.createAndShowGUI();
	}
}
