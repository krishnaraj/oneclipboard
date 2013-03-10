package com.cb.oneclipboard.lib.socket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.cb.oneclipboard.lib.Message;
import com.cb.oneclipboard.lib.SocketListener;
import com.cb.oneclipboard.lib.User;

public class ClipboardConnector {

	private static Socket clientSocket;
	private static ObjectInputStream objInputStream;
	private static ObjectOutputStream objOutputStream;

	public static void connect(final String server, final int port, User user, final SocketListener messageListener) {
		final boolean listening = true;
		Thread listenerThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					clientSocket = new Socket(server, port);
					/* The objOutputStream needs to created and flushed before the objInputStream can be created*/
					objOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
					objOutputStream.flush();
					objInputStream = new ObjectInputStream(clientSocket.getInputStream());
					System.out.println("connected to " + server + ":" + port);
					messageListener.onConnect();
					
					while(listening){
						Message message = (Message) objInputStream.readObject();
						messageListener.onMessageReceived(message);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try{
						objInputStream.close();
						objOutputStream.close();
						clientSocket.close();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			
		}, "Incoming message listener thread");
		
		listenerThread.start();
	}
	
	public static void send(Message message){
		try{
			if (objOutputStream != null) {
				objOutputStream.writeObject(message);
				objOutputStream.flush();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
