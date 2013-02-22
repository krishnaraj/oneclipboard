package com.cb.oneclipboard.server;

import com.cb.oneclipboard.lib.ApplicationProperties;
import com.cb.oneclipboard.lib.DefaultPropertyLoader;
import com.cb.oneclipboard.lib.Message;
import com.cb.oneclipboard.lib.MessageListener;
import com.cb.oneclipboard.lib.MessageSender;
import com.cb.oneclipboard.lib.socket.ClipboardConnector;

public class Server {

	public static final String[] PROP_LIST = { "config.properties" };

	public static void main(String[] args) {
		ApplicationProperties.loadProperties(PROP_LIST, new DefaultPropertyLoader());

		ClipboardConnector.startListening(4444, new MessageListener() {

			@Override
			public void onMessageReceived(String ip, Message message) {
				switch (message.getMessageType()) {
				case REGISTER: Registery.register(ip); break;
				case TEXT: broadcastMessage(ip, message); break;
				default: System.out.println("Unknown message type: " + message.getMessageType());
				}

			}
		});

	}
	
	private static void broadcastMessage(String hostAddress, Message message) {
		for (String ip : Registery.getClients()){
			if(!hostAddress.equals(ip)){
				MessageSender.send(ip, 4444, message);
			}
		}
		
	}
}