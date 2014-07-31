package com.cb.oneclipboard.desktop.gui;

import javax.swing.JDialog;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPanel;

import java.awt.Component;

import javax.swing.JPasswordField;

import com.cb.oneclipboard.desktop.ApplicationConstants.Property;
import com.cb.oneclipboard.desktop.Client;
import com.cb.oneclipboard.desktop.Utilities;
import com.cb.oneclipboard.lib.User;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class LoginDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JPasswordField passwordField;

	public LoginDialog() {
		super(null, java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
		setModal(true);
		setIconImage(UIUtility.createImage("/images/logo.png", "OneClipboard"));
		setTitle(Utilities.getFullApplicationName());
		getContentPane().setBackground(Color.BLACK);
		setBackground(Color.BLACK);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		JLabel lblEnterAnyUsernamepassword = new JLabel(
				"<html>Enter any username/password but make sure all your other devices share the same.</html>");
		lblEnterAnyUsernamepassword.setAlignmentY(Component.TOP_ALIGNMENT);
		lblEnterAnyUsernamepassword.setForeground(Color.WHITE);
		lblEnterAnyUsernamepassword.setBounds(10, -12, 365, 68);
		getContentPane().add(lblEnterAnyUsernamepassword);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setForeground(Color.WHITE);
		lblUsername.setBounds(10, 46, 102, 15);
		getContentPane().add(lblUsername);

		textField = new JTextField();
		textField.setBounds(93, 41, 114, 25);
		getContentPane().add(textField);
		textField.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setBounds(10, 77, 70, 15);
		getContentPane().add(lblPassword);

		passwordField = new JPasswordField();
		passwordField.setColumns(10);
		passwordField.setBounds(93, 72, 114, 25);
		getContentPane().add(passwordField);

		final JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String userName = textField.getText();
				String password = String.valueOf(passwordField.getPassword());
				if (userName.trim().length() == 0 || password.trim().length() == 0) {
					return;
				}
				LoginDialog.this.dispose();
				Client.propertyChangeSupport.firePropertyChange(Property.LOGIN, null, new User(userName, password));
			}
		});
		btnLogin.setBounds(10, 111, 117, 25);

		passwordField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					btnLogin.doClick();
				}

			}
		});

		getContentPane().add(btnLogin);
		setResizable(false);
		setPreferredSize(new Dimension(399, 175));
		setSize(200, 200);
		setLocationRelativeTo(null);
		pack();
	}
}
