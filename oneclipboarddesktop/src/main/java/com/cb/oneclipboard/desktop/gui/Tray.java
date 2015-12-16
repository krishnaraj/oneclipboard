package com.cb.oneclipboard.desktop.gui;

import com.cb.oneclipboard.desktop.ApplicationConstants.Property;
import com.cb.oneclipboard.desktop.Client;
import com.cb.oneclipboard.desktop.Utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tray {
    private static final Logger LOG = Logger.getLogger(Tray.class.getName());

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
                try {
                    createAndShowGUI();
                } catch (Exception e) {
                    LOG.log(Level.SEVERE, "Error when creating GUI", e);
                }
            }
        });
    }

    public static void createAndShowGUI() throws Exception {
        // Check the SystemTray support
        if (!SystemTray.isSupported()) {
            LOG.info("SystemTray is not supported");
            return;
        }
        final PopupMenu popup = new PopupMenu();
        final Image iconBlue = UIUtility.createImage("/images/logo.png", "OneClipboard online.");
        final Image iconRed = UIUtility.createImage("/images/logo-red.png", "OneClipboard offline.");
        final TrayIcon trayIcon = new TrayIcon(iconBlue);
        final SystemTray tray = SystemTray.getSystemTray();

        // Create a popup menu components
        MenuItem aboutItem = new MenuItem("About");
        MenuItem logoutItem = new MenuItem("Log Out");
        MenuItem exitItem = new MenuItem("Exit");
        // Initially the client is started, so show stop button.
        MenuItem startStopItem = new MenuItem("Stop");
        startStopItem.setActionCommand(Property.STOP.name());

        // Add components to popup menu
        popup.add(startStopItem);
        popup.add(aboutItem);
        popup.add(logoutItem);
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);

        try {
            trayIcon.setImageAutoSize(true);
            tray.add(trayIcon);
        } catch (AWTException e) {
            LOG.log(Level.SEVERE, "TrayIcon could not be added.", e);
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

        logoutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearTray();
                Client.propertyChangeSupport.firePropertyChange(Property.LOGOUT, null, null);
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
        JOptionPane.showMessageDialog(null, Utilities.getFullApplicationName(), "About", JOptionPane.PLAIN_MESSAGE);
    }

    public static void clearTray() {
        final TrayIcon[] icons = SystemTray.getSystemTray().getTrayIcons();
        for (final TrayIcon icon : icons) {
            SystemTray.getSystemTray().remove(icon);
        }
    }
}
