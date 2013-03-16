package com.cb.oneclipboard.desktop.gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.cb.oneclipboard.desktop.ApplicationConstants;
import com.cb.oneclipboard.desktop.ApplicationConstants.Property;

public class ApplicationUI {
	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

	public void show() {

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

	private void createAndShowGUI() {
		// Show the login dialog
		LoginDialog loginDialog = new LoginDialog();
		loginDialog.setVisible(true);
		changeSupport.firePropertyChange(Property.LOGIN.name(), null, loginDialog.getUser());
		// System tray
		Tray.createAndShowGUI();
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}
}
