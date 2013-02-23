package com.cb.oneclipboard.lib;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class MessageSender {

	public static void send(final Header header, final String text) {
		Message message = new Message(text, MessageType.TEXT, header.getReplyPort());
		send(header.getServerAddress(), header.getServerPort(), message);
	}

	public static void send(final String server, final int port, final Message message) {
		Thread senderThread = new Thread(new Runnable() {

			@Override
			public void run() {
				ObjectOutputStream oos = null;
				try {
					SocketAddress sockaddr = new InetSocketAddress(server, port);
					Socket socket = new Socket(); 
					socket.connect(sockaddr);
					OutputStream os = socket.getOutputStream();  
					oos = new ObjectOutputStream(os);  
					oos.writeObject(message); // write the message to output stream

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

	public static void sendRegisterMessage(Header header) {
		Message message = new Message("register", MessageType.REGISTER, header.getReplyPort());
		send(header.getServerAddress(), header.getServerPort(), message);
	}
}
