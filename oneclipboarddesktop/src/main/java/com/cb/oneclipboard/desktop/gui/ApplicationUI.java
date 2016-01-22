package com.cb.oneclipboard.desktop.gui;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationUI {
    private static final Logger LOG = Logger.getLogger(ApplicationUI.class.getName());

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
                try {
                    Tray.createAndShowGUI();
                } catch (Exception e) {
                    LOG.log(Level.SEVERE, "Error creating tray.", e);
                }
            }
        });
    }

    public void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

}
