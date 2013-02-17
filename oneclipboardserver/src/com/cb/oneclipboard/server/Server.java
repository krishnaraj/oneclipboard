package com.cb.oneclipboard.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.cb.oneclipboard.lib.ApplicationProperties;
import com.cb.oneclipboard.lib.DefaultPropertyLoader;
import com.cb.oneclipboard.lib.Message;
import com.cb.oneclipboard.lib.MessageSender;

public class Server {
	
	public static final String[] PROP_LIST = {"config.properties"};

	private static ServerSocket serverSocket;
	private static Socket clientSocket;
	private static InputStream inputStream;
	private static ObjectInputStream objInputStream;
	private static Message message;

	public static void main(String[] args) {
		ApplicationProperties.loadProperties(PROP_LIST, new DefaultPropertyLoader());

		try {
			serverSocket = new ServerSocket(4444); // Server socket

		} catch (IOException e) {
			System.out.println("Could not listen on port: 4444");
		}

		System.out.println("Server started. Listening on port 4444");

		while (true) {
			try {

				clientSocket = serverSocket.accept(); // accept the client connection
				inputStream = clientSocket.getInputStream();
				objInputStream = new ObjectInputStream(inputStream); // get the client message
				message = (Message)objInputStream.readObject();

				System.out.println(message);
				inputStream.close();
				clientSocket.close();
				
				String hostAddress = clientSocket.getInetAddress().getHostAddress();
				System.out.println("hostAddress: " + hostAddress);
				
				MessageSender.send(
						ApplicationProperties.getStringProperty("server"), 
						ApplicationProperties.getIntProperty("port"),
						message);

			} catch (Exception ex) {
				System.out.println("Problem in message reading");
			}
		}

	}
}