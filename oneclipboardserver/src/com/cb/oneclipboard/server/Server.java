package com.cb.oneclipboard.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;

import com.cb.oneclipboard.lib.ApplicationProperties;
import com.cb.oneclipboard.lib.DefaultPropertyLoader;
import com.cb.oneclipboard.server.admin.AdminServer;

public class Server {

	public static final String[] PROP_LIST = { "config.properties" };
	private static int serverPort;

	public static void main(String[] args) throws Exception {
		pipeSysoutToFile();
		init(args);

		ServerSocket serverSocket = null;
		boolean listening = true;

		try {
			serverSocket = new ServerSocket(serverPort);
			System.out.println("Server started on port " + serverPort);
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + serverPort);
			System.exit(-1);
		}

		while (listening) {
			ServerThread serverThread = new ServerThread(serverSocket.accept());
			Registery.register(serverThread);
			serverThread.start();
		}

	}

	private static void init(String[] args) {
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
			e.printStackTrace();
		}
	}

	private static void pipeSysoutToFile() throws FileNotFoundException {
		File file = new File("sysout.log");
		PrintStream printStream = new PrintStream(new FileOutputStream(file));
		System.setOut(printStream);
		System.setErr(printStream);
	}
}