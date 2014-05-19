package com.cb.oneclipboard.lib.socket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cb.oneclipboard.lib.Message;
import com.cb.oneclipboard.lib.MessageType;
import com.cb.oneclipboard.lib.SocketListener;
import com.cb.oneclipboard.lib.User;

public class ClipboardConnector {
	private final static Logger LOGGER = Logger.getLogger(ClipboardConnector.class.getName());

	private static Socket clientSocket;
	private static ObjectInputStream objInputStream;
	private static ObjectOutputStream objOutputStream;

	private static final int MAX_RECONNECT_ATTEMPTS = 3;
	private static int reconnectCounter = 0;
	
	private static volatile boolean connected = false;

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
				} catch (Exception e) {
					// try reconnecting
					reconnectCounter++;
					if (reconnectCounter <= MAX_RECONNECT_ATTEMPTS) {
						LOGGER.info("Reconnect attempt " + reconnectCounter);
						connect(server, port, user, messageListener);
						return;
					}
					LOGGER.log(Level.SEVERE, "Unable to connect", e);
					connected = false;
					return;
				}
				LOGGER.info("connected to " + server + ":" + port);
				connected = true;

				// resert counter
				reconnectCounter = 0;

				messageListener.onConnect();

				try {
					while (listening) {
						if ( objInputStream != null ) {
							Message message = (Message) objInputStream.readObject();
							processMessage( message, messageListener );
						}
					}

				} catch (SocketException e) {
					LOGGER.severe(e.getMessage());
				} catch (Exception e) {
					LOGGER.log(Level.SEVERE, "Exception when listening for message", e);
				} finally {
					close();
					connected = false;
					messageListener.onDisconnect();
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
			if ( isConnected() && objOutputStream != null ) {
				objOutputStream.writeObject(message);
				objOutputStream.flush();
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Exception when sending message", e);
		}
	}

	public static void close() {
		try {
			objInputStream.close();
			objOutputStream.close();
			clientSocket.close();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Exception when closing resources", e);
		}
	}
	
	public static boolean isConnected() {
		return connected;
	}
	
	public static String getServerName() {
		try{
			return clientSocket.getInetAddress().getHostName();
		} catch( Exception e ) {
			LOGGER.log(Level.FINE, "Exception when getting host name", e);
		}
		return "";
	}
}
