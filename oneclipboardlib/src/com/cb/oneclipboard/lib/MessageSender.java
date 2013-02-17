package com.cb.oneclipboard.lib;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MessageSender {

	public static void send(final String server, final int port, final String text) {
		Message message = new Message(server, text, MessageType.TEXT);
		send(server, port, message);
	}

	public static void send(final String server, final int port, final Object object) {
		Thread senderThread = new Thread(new Runnable() {

			@Override
			public void run() {
				ObjectOutputStream oos = null;
				try {

					Socket socket = new Socket(server, port); // connect to server
					OutputStream os = socket.getOutputStream();  
					oos = new ObjectOutputStream(os);  
					oos.writeObject(object); // write the message to output stream

					oos.flush();
					oos.close();
					os.close();
					socket.close();

				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e){
					e.printStackTrace();
				}

			}
		}, "senderThread");

		senderThread.start();
	}

	public static void register(final String server, final int port) {
		Message message = new Message(server, "register", MessageType.REGISTER);
		send(server, port, message);
	}
}
