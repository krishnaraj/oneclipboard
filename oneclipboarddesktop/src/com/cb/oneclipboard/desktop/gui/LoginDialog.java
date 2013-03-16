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

public class LoginDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTextField userNameTxt;
	private JTextField passwordTxt;
	
	public LoginDialog() {
		getContentPane().setBackground(Color.BLACK);
		setBackground(Color.BLACK);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setSize(new Dimension(400, 300));
		setSize(new Dimension(399, 156));
		setResizable(false);
		setPreferredSize(new Dimension(100, 100));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{197, 197, 0};
		gridBagLayout.rowHeights = new int[]{45, 45, 45, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JLabel usenameLabel = new JLabel("Username");
		usenameLabel.setBackground(Color.BLACK);
		usenameLabel.setForeground(Color.WHITE);
		GridBagConstraints gbc_usenameLabel = new GridBagConstraints();
		gbc_usenameLabel.insets = new Insets(0, 0, 5, 5);
		gbc_usenameLabel.gridx = 0;
		gbc_usenameLabel.gridy = 0;
		getContentPane().add(usenameLabel, gbc_usenameLabel);
		
		userNameTxt = new JTextField();
		GridBagConstraints gbc_userNameTxt = new GridBagConstraints();
		gbc_userNameTxt.fill = GridBagConstraints.BOTH;
		gbc_userNameTxt.insets = new Insets(0, 0, 5, 0);
		gbc_userNameTxt.gridx = 1;
		gbc_userNameTxt.gridy = 0;
		getContentPane().add(userNameTxt, gbc_userNameTxt);
		userNameTxt.setColumns(10);
		
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setForeground(Color.WHITE);
		GridBagConstraints gbc_passwordLabel = new GridBagConstraints();
		gbc_passwordLabel.insets = new Insets(0, 0, 5, 5);
		gbc_passwordLabel.gridx = 0;
		gbc_passwordLabel.gridy = 1;
		getContentPane().add(passwordLabel, gbc_passwordLabel);
		
		passwordTxt = new JTextField();
		GridBagConstraints gbc_passwordTxt = new GridBagConstraints();
		gbc_passwordTxt.fill = GridBagConstraints.BOTH;
		gbc_passwordTxt.insets = new Insets(0, 0, 5, 0);
		gbc_passwordTxt.gridx = 1;
		gbc_passwordTxt.gridy = 1;
		getContentPane().add(passwordTxt, gbc_passwordTxt);
		passwordTxt.setColumns(10);
		
		JButton loginButton = new JButton("Login");
		loginButton.setActionCommand("Login");
		GridBagConstraints gbc_loginButton = new GridBagConstraints();
		gbc_loginButton.insets = new Insets(0, 0, 5, 0);
		gbc_loginButton.gridwidth = 2;
		gbc_loginButton.fill = GridBagConstraints.VERTICAL;
		gbc_loginButton.gridx = 0;
		gbc_loginButton.gridy = 2;
		getContentPane().add(loginButton, gbc_loginButton);
	}

}
