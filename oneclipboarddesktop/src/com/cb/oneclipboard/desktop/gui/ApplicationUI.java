package com.cb.oneclipboard.desktop.gui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ApplicationUI {

	public ApplicationUI() {
		/* Use an appropriate Look and Feel */
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			// UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void showLogin() {
		// Schedule a job for the event-dispatching thread:
		// adding TrayIcon.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	private void createAndShowGUI() {
		// Show the login dialog
		LoginDialog loginDialog = new LoginDialog();
		loginDialog.setVisible(true);
	}

	public void createAndShowTray() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// System tray
				Tray.createAndShowGUI();
			}
		});
	}

}
