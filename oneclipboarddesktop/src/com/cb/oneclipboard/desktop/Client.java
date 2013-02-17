package com.cb.oneclipboard.desktop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import com.cb.oneclipboard.lib.ApplicationProperties;
import com.cb.oneclipboard.lib.MessageSender;

public class Client {

	private static ServerSocket serverSocket;
	private static Socket clientSocket;
	private static InputStreamReader inputStreamReader;
	private static BufferedReader bufferedReader;
	private static String message;

	public static void main(String[] args) {
		register();

		try {
			serverSocket = new ServerSocket(5555); // Server socket

		} catch (IOException e) {
			System.out.println("Could not listen on port: 5555");
		}

		System.out.println("Server started. Listening on port 5555");

		while (true) {
			try {

				clientSocket = serverSocket.accept(); // accept the client connection
				inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
				bufferedReader = new BufferedReader(inputStreamReader); // get the client message
				message = bufferedReader.readLine();

				System.out.println("Received: " + message);
				inputStreamReader.close();
				clientSocket.close();
				
				TextTransfer textTransfer = new TextTransfer();
				textTransfer.setClipboardContents(message);
				
			} catch (IOException ex) {
				System.out.println("Problem in message reading");
			}
		}

	}
	
	private static void register() {
		try{
			MessageSender.register(
					ApplicationProperties.getStringProperty("server"), 
					ApplicationProperties.getIntProperty("port"));
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
