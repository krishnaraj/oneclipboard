package com.cb.oneclipboard.lib.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.cb.oneclipboard.lib.Message;
import com.cb.oneclipboard.lib.MessageListener;

public class ClipboardConnector {

	private static ServerSocket serverSocket;
	private static Socket clientSocket;
	private static InputStream inputStream;
	private static ObjectInputStream objInputStream;
	private static Message message;

	public static void startListening(final int port, final MessageListener messageListener) {
		Thread listenerThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					serverSocket = new ServerSocket(port); // Server socket

				} catch (IOException e) {
					System.out.println("Could not listen on port: " + port);
					System.exit(0);
				}

				System.out.println("Server started. Listening on port " + port);

				while (true) {
					try {

						clientSocket = serverSocket.accept(); // accept the client connection
						inputStream = clientSocket.getInputStream();
						objInputStream = new ObjectInputStream(inputStream); // get the client message
						message = (Message) objInputStream.readObject();

						inputStream.close();
						clientSocket.close();
						
						String ip = clientSocket.getInetAddress().getHostAddress();
						messageListener.onMessageReceived(ip, message);

					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
			
		}, "Incoming message listener thread");
		
		listenerThread.start();
	}
}
