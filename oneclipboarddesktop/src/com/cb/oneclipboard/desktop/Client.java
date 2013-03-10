package com.cb.oneclipboard.desktop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.cb.oneclipboard.lib.ApplicationProperties;
import com.cb.oneclipboard.lib.Callback;
import com.cb.oneclipboard.lib.DefaultPropertyLoader;
import com.cb.oneclipboard.lib.Message;
import com.cb.oneclipboard.lib.SocketListener;
import com.cb.oneclipboard.lib.socket.ClipboardConnector;

public class Client {

	private static String serverAddress = null;
	private static int serverPort;
	private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public static final String[] PROP_LIST = { "config.properties" };

	public static void main(String[] args) {
		pipeSysoutToFile();
		final TextTransfer textTransfer = new TextTransfer();
		
		
		// Initialize properties
		ApplicationProperties.loadProperties(PROP_LIST, new DefaultPropertyLoader());
		serverAddress = ApplicationProperties.getStringProperty("server");
		serverPort = ApplicationProperties.getIntProperty("server_port");
		
		if (args.length > 0) {
			try {
				serverPort = Integer.parseInt(args[0]);
			} catch (Exception e) {
			}
		}
		
		// Poll the clipboard for changes
		final ClipboardPollTask clipboardPollTask = new ClipboardPollTask(textTransfer, new Callback() {

			@Override
			public void execute(Object object) {
				String clipboardText = (String) object;
				ClipboardConnector.send(new Message(clipboardText));
			}
		});
		
		// Listen for clipboard content from other clients
		ClipboardConnector.connect(serverAddress, serverPort, new SocketListener() {

			@Override
			public void onMessageReceived(Message message) {
				clipboardPollTask.updateClipboardContent(message.getText());
				textTransfer.setClipboardContents(message.getText());
			}

		});
		
		// Run the poll task every 2 seconds
		final ScheduledFuture<?> pollHandle = scheduler.scheduleAtFixedRate(clipboardPollTask, 1, 2, TimeUnit.SECONDS);

	}
	
	private static void pipeSysoutToFile() {
		try {
			String logFileName = System.getProperty("user.home") + File.separator + "oneclipboard.log";
			File file = new File(logFileName);
			PrintStream printStream = new PrintStream(new FileOutputStream(file));
			System.setOut(printStream);
			System.setErr(printStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
