package com.cb.oneclipboard.desktop.gui;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.cb.oneclipboard.desktop.ApplicationConstants.Property;
import com.cb.oneclipboard.desktop.Client;

public class Tray {
	public static void main(String[] args) {
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

	public static void createAndShowGUI() {
		// Check the SystemTray support
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}
		final PopupMenu popup = new PopupMenu();
		final Image iconBlue = createImage("/logo.png", "tray icon");
		final Image iconRed = createImage("/logo-red.png", "tray icon");
		final TrayIcon trayIcon = new TrayIcon(iconBlue);
		final SystemTray tray = SystemTray.getSystemTray();

		// Create a popup menu components
		MenuItem aboutItem = new MenuItem("About");
		MenuItem exitItem = new MenuItem("Exit");
		// Initially the client is started, so show stop button.
		MenuItem startStopItem = new MenuItem("Stop");
		startStopItem.setActionCommand(Property.STOP.name());

		// Add components to popup menu
		popup.add(startStopItem);
		popup.add(aboutItem);
		popup.add(exitItem);

		trayIcon.setPopupMenu(popup);

		try {
			trayIcon.setImageAutoSize(true);
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
			return;
		}

		trayIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAboutDialog();
			}
		});

		aboutItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAboutDialog();
			}
		});

		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tray.remove(trayIcon);
				System.exit(0);
			}
		});

		startStopItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Property property = Property.valueOf(e.getActionCommand());
				MenuItem sourceItem = ((MenuItem) e.getSource());
				if (property == Property.STOP) {
					sourceItem.setLabel("Start");
					sourceItem.setActionCommand(Property.START.name());
					trayIcon.setImage(iconRed);
				} else {
					sourceItem.setLabel("Stop");
					sourceItem.setActionCommand(Property.STOP.name());
					trayIcon.setImage(iconBlue);
				}

				Client.propertyChangeSupport.firePropertyChange(Property.valueOf(e.getActionCommand()), null, null);

			}
		});
	}

	public static void showAboutDialog() {
		JOptionPane.showMessageDialog(null, "OneClipboard v1.0", "About", JOptionPane.PLAIN_MESSAGE);
	}

	// Obtain the image URL
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
