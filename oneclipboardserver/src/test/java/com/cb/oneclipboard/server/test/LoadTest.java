package com.cb.oneclipboard.server.test;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.Test;

import com.cb.oneclipboard.lib.Message;
import com.cb.oneclipboard.lib.SocketListener;
import com.cb.oneclipboard.lib.User;
import com.cb.oneclipboard.lib.socket.ClipboardConnector;

public class LoadTest {
	private static final int NO_OF_CLIENTS = 100;
	private String serverAddress = null;
	private int serverPort;
	private User user = null;

	@Before
	public void setUp() throws Exception {
		serverAddress = "localhost";
		serverPort = 4545;
		user = new User("testuser", "testuser");
	}

	@Test
	public void test() throws InterruptedException {
		final CountDownLatch latch = new CountDownLatch(NO_OF_CLIENTS);
		
		for (int i = 0; i < NO_OF_CLIENTS; i++) {
			ClipboardConnector.connect(serverAddress, serverPort, user, new SocketListener() {

				@Override
				public void onMessageReceived(Message message) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onDisconnect() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onConnect() {
					latch.countDown();
				}
			});
		}
		
		latch.await();
		
		Thread.sleep(1000 * 60);
		
		assertTrue(true);
	}

}
