package com.cb.oneclipboard.lib;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClipboardConnector {

	private static ServerSocket serverSocket;
	private static Socket clientSocket;
	private static InputStream inputStream;
	private static ObjectInputStream objInputStream;
	private static Message message;

	public static void startListening(int port) {
		try {
			serverSocket = new ServerSocket(port); // Server socket

		} catch (IOException e) {
			System.out.println("Could not listen on port: " + port);
		}

		System.out.println("Server started. Listening on port " + port);

		while (true) {
			try {

				clientSocket = serverSocket.accept(); // accept the client connection
				inputStream = clientSocket.getInputStream();
				objInputStream = new ObjectInputStream(inputStream); // get the client message
				message = (Message) objInputStream.readObject();

				System.out.println(message);
				inputStream.close();
				clientSocket.close();

			} catch (Exception ex) {
				System.out.println("Problem in message reading");
			}
		}
	}
}
