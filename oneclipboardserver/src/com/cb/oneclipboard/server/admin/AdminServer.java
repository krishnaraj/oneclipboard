package com.cb.oneclipboard.server.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class AdminServer {
	private static final int serverPort = 4040;

	public static void start(String args[]) throws IOException {
		Thread adminThread = new Thread(new Runnable() {
			
			@Override
			public void run() {

				ServerSocket serverSocket = null;

				try {
					serverSocket = new ServerSocket(serverPort);
					System.out.println("Admin Server started on port " + serverPort);
				} catch (IOException e) {
					System.err.println("Could not listen on port: " + serverPort);
				}

				Socket clientSocket = null;
				try {
					clientSocket = serverSocket.accept();
				} catch (IOException e) {
					System.err.println("Accept failed.");
				}

				try {
					PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					String inputLine, outputLine;
					AdminCommandProcessor commandProcessor = new AdminCommandProcessor();

					while ((inputLine = in.readLine()) != null) {
						outputLine = commandProcessor.processCommand(inputLine);
						out.println(outputLine);
						if (outputLine.equals("exit"))
							break;
					}

					out.close();
					in.close();
					clientSocket.close();
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, "Admin Server Thread");
		
		adminThread.start();
	}
}
