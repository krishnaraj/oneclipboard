package com.cb.oneclipboard.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import com.cb.oneclipboard.lib.ApplicationProperties;
import com.cb.oneclipboard.lib.DefaultPropertyLoader;
import com.cb.oneclipboard.lib.Message;
import com.cb.oneclipboard.lib.MessageSender;
import com.cb.oneclipboard.lib.SocketListener;
import com.cb.oneclipboard.lib.socket.ClipboardConnector;

public class Server {

	public static final String[] PROP_LIST = { "config.properties" };
	private static int serverPort;

	public static void main(String[] args) throws Exception {
		pipeSysoutToFile();

		ApplicationProperties.loadProperties(PROP_LIST, new DefaultPropertyLoader());
		serverPort = ApplicationProperties.getIntProperty("server_port");

		if (args.length > 0) {
			try {
				serverPort = Integer.parseInt(args[0]);
			} catch (Exception e) {
			}
		}

		ClipboardConnector.startListening(serverPort, new SocketListener() {

			@Override
			public void onMessageReceived(String ip, Message message) {
				switch (message.getMessageType()) {
				case REGISTER:
					Registery.register(ip, message.getReplyPort());
					break;
				case TEXT:
					broadcastMessage(ip, message);
					break;
				default:
					System.out.println("Unknown message type: " + message.getMessageType());
				}

			}

			@Override
			public void onPortReady(int replyPort) {

			}
		});

	}

	private static void broadcastMessage(String hostAddress, Message message) {
		for (ClientData clientData : Registery.getClients()) {
			String source = hostAddress + ":" + message.getReplyPort();
			String destination = clientData.getIp() + ":" + clientData.getPort();
			if (!source.equals(destination)) {
				System.out.println("Broadcasting '" + message.getText() + "' to " + clientData.getIp() + ":" + clientData.getPort());
				MessageSender.send(clientData.getIp(), clientData.getPort(), message);
			}
		}

	}

	private static void pipeSysoutToFile() throws FileNotFoundException {
		File file = new File("sysout.log");
		PrintStream printStream = new PrintStream(new FileOutputStream(file));
		System.setOut(printStream);
		System.setErr(printStream);
	}
}