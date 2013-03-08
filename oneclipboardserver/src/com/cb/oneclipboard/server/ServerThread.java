package com.cb.oneclipboard.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.cb.oneclipboard.lib.Message;

public class ServerThread extends Thread {
	Socket socket = null;
	ObjectInputStream objInputStream = null;
	ObjectOutputStream objOutputStream = null;

	public ServerThread(Socket socket) throws Exception{
		super("ServerThread");
		this.socket = socket;
		objInputStream = new ObjectInputStream(socket.getInputStream());
		objOutputStream = new ObjectOutputStream(socket.getOutputStream());
	}

	@Override
	public void run() {
		try {
			Message message = null;
			while ((message = (Message) objInputStream.readObject()) != null) {
				System.out.println("Received '" + message.getText() + "' from " + socket.getInetAddress());
				for (ServerThread serverThread : Registery.getClientSockets()) {
					if (!this.socket.equals(serverThread.socket)) {
						System.out.println("Sending '" + message.getText() + "' to " + serverThread.socket.getInetAddress());
						try {
							serverThread.objOutputStream.writeObject(message);
							serverThread.objOutputStream.flush();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
