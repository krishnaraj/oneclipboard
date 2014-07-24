package com.cb.oneclipboard.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cb.oneclipboard.lib.ApplicationProperties;
import com.cb.oneclipboard.lib.DefaultPropertyLoader;
import com.cb.oneclipboard.server.admin.AdminServer;
import com.cb.oneclipboard.server.logging.LevelBasedFileHandler;

public class Server {
	private final static Logger LOGGER = Logger.getLogger(Server.class.getName());

	public static final String[] PROP_LIST = { "config.properties" };
	private static int serverPort;

	public static void main(String[] args) throws Exception {
		init(args);

		ServerSocket serverSocket = null;
		boolean listening = true;

		try {
			serverSocket = new ServerSocket(serverPort);
			LOGGER.info("Server started on port " + serverPort);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Could not listen on port: " + serverPort);
			System.exit(-1);
		}

		while (listening) {
			ServerThread serverThread = null;
			try {
				serverThread = new ServerThread(serverSocket.accept());
				Registery.register(serverThread);
				serverThread.start();
			} catch (Exception e) {
				try {
					serverThread.close();
				} catch (Exception ex) {
					LOGGER.log(Level.WARNING, "Unable to close server thread properly " + ex.getMessage());
				}
				LOGGER.log(Level.WARNING, "Connection lost: " + e.getMessage());
			}
		}

	}

	private static void init(String[] args) throws SecurityException, IOException {
		// set up loggers
		Logger.getLogger("").addHandler(new LevelBasedFileHandler("oneclipboardserver.log", Level.INFO));
		Logger.getLogger("").addHandler(new LevelBasedFileHandler("oneclipboardserver.err", Level.SEVERE));

		// Load properties
		ApplicationProperties.loadProperties(PROP_LIST, new DefaultPropertyLoader());
		serverPort = ApplicationProperties.getIntProperty("server_port");

		if (args.length > 0) {
			try {
				serverPort = Integer.parseInt(args[0]);
			} catch (Exception e) {
			}
		}

		// Start admin server
		try {
			AdminServer.start(args);
			ServerThreadCleaner.start(args);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error starting admin server", e);
		}
	}

}