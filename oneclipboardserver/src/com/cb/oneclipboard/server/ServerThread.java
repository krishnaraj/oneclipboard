package com.cb.oneclipboard.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.cb.oneclipboard.lib.Message;
import com.cb.oneclipboard.lib.MessageType;
import com.cb.oneclipboard.lib.User;

public class ServerThread extends Thread {
	Socket socket = null;
	ObjectInputStream objInputStream = null;
	ObjectOutputStream objOutputStream = null;
	User user = null;

	public ServerThread(Socket socket) throws Exception {
		super("ServerThread");
		this.socket = socket;
		objInputStream = new ObjectInputStream(socket.getInputStream());
		objOutputStream = new ObjectOutputStream(socket.getOutputStream());
		objOutputStream.flush();
	}

	@Override
	public void run() {
		try {
			Message message = null;
			while ((message = (Message) objInputStream.readObject()) != null) {
				System.out.println(String.format("Received '%s' from %s at %s", message.getText(), message.getUser().getUserName(), getHostAddress()));

				if (message.getMessageType() == MessageType.REGISTER) {
					user = message.getUser();
				} else if (message.getMessageType() == MessageType.CLIPBOARD_TEXT && user != null) { // if we haven't received the register message than ignore the text messages
					for (ServerThread serverThread : Registery.getClientSockets()) {
						if (!this.socket.equals(serverThread.socket) && this.user.equals(serverThread.user)) {
							System.out.println(String.format("Sending '%s' to %s at %s", message.getText(),
									serverThread.user.getUserName(), serverThread.getHostAddress()));
							try {
								serverThread.objOutputStream.writeObject(message);
								serverThread.objOutputStream.flush();
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}
				}
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
			Registery.getClientSockets().remove(this);
		}
	}

	public String getHostAddress() {
		return socket.getInetAddress().getHostAddress();
	}
}
