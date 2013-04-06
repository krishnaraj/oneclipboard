package com.cb.oneclipboard.lib.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.cb.oneclipboard.lib.Message;
import com.cb.oneclipboard.lib.MessageType;
import com.cb.oneclipboard.lib.SocketListener;
import com.cb.oneclipboard.lib.User;

public class ClipboardConnector {

	private static Socket clientSocket;
	private static ObjectInputStream objInputStream;
	private static ObjectOutputStream objOutputStream;

	private static final int MAX_RECONNECT_ATTEMPTS = 3;
	private static int reconnectCounter = 0;

	public static void connect(final String server, final int port, final User user, final SocketListener messageListener) {
		final boolean listening = true;
		Thread listenerThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					clientSocket = new Socket(server, port);
					/*
					 * The objOutputStream needs to be created and flushed
					 * before the objInputStream can be created
					 */
					objOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
					objOutputStream.flush();
					objInputStream = new ObjectInputStream(clientSocket.getInputStream());
					System.out.println("connected to " + server + ":" + port);

					// resert counter
					reconnectCounter = 0;

					messageListener.onConnect();

					while (listening) {
						Message message = (Message) objInputStream.readObject();
						processMessage(message, messageListener);
					}

				} catch (IOException e) {
					// try reconnecting
					reconnectCounter++;
					System.err.println("Connection lost! reconnect attempt " + reconnectCounter);
					if (reconnectCounter <= MAX_RECONNECT_ATTEMPTS) {
						connect(server, port, user, messageListener);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						objInputStream.close();
						objOutputStream.close();
						clientSocket.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		}, "Incoming message listener thread");

		listenerThread.start();
	}

	@SuppressWarnings("incomplete-switch")
	private static void processMessage(Message message, SocketListener listener) {
		switch (message.getMessageType()) {
		case CLIPBOARD_TEXT:
			listener.onMessageReceived(message);
			break;
		case PING:
			// Server is checking if connection is alive, ping back to say yes.
			send(new Message("ping", MessageType.PING, message.getUser()));
			break;
		}
	}

	public static void send(Message message) {
		try {
			if (objOutputStream != null) {
				objOutputStream.writeObject(message);
				objOutputStream.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
