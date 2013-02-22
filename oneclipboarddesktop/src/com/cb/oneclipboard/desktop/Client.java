package com.cb.oneclipboard.desktop;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.cb.oneclipboard.lib.ApplicationProperties;
import com.cb.oneclipboard.lib.Callback;
import com.cb.oneclipboard.lib.DefaultPropertyLoader;
import com.cb.oneclipboard.lib.Message;
import com.cb.oneclipboard.lib.MessageListener;
import com.cb.oneclipboard.lib.MessageSender;
import com.cb.oneclipboard.lib.socket.ClipboardConnector;

public class Client {

	private static String serverAddress = null;
	private static int port;
	private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public static final String[] PROP_LIST = { "config.properties" };

	public static void main(String[] args) {
		final TextTransfer textTransfer = new TextTransfer();
		
		// Initialize properties
		ApplicationProperties.loadProperties(PROP_LIST, new DefaultPropertyLoader());
		serverAddress = ApplicationProperties.getStringProperty("server");
		port = ApplicationProperties.getIntProperty("port");
		
		// Send a register message to the server so that the client ip can be registered
		MessageSender.sendRegisterMessage(serverAddress, port);
		
		// Poll the clipboard for changes
		final ClipboardPollTask clipboardPollTask = new ClipboardPollTask(textTransfer, new Callback() {

			@Override
			public void execute(Object object) {
				String clipboardText = (String) object;
				MessageSender.send(serverAddress, port, clipboardText);
			}
		});
		
		// Listen for clipboard content from other clients
		ClipboardConnector.startListening(ApplicationProperties.getIntProperty("port"), new MessageListener() {

			@Override
			public void onMessageReceived(String ip, Message message) {
				clipboardPollTask.updateClipboardContent(message.getText());
				textTransfer.setClipboardContents(message.getText());
			}
		});
		
		// Run the poll task every 2 seconds
		final ScheduledFuture<?> pollHandle = scheduler.scheduleAtFixedRate(clipboardPollTask, 0, 2, TimeUnit.SECONDS);

	}
}
