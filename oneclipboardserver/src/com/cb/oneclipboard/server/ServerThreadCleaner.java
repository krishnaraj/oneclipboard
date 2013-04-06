package com.cb.oneclipboard.server;

import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ServerThreadCleaner {
	private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public static void start(String args[]) throws Exception {
		final ScheduledFuture<?> scheduledCleaner = scheduler.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				System.out.println("ServerThreadCleaner running...");
				Iterator<ServerThread> iterator = Registery.getClientSockets().iterator();
				while (iterator.hasNext()) {
					ServerThread serverThread = iterator.next();
					try {
						System.out.println("Pinging " + serverThread.user.getUserName() + "@" + serverThread.getHostAddress());
						serverThread.ping();
					} catch (Exception e) {
						System.err.println(String.format("Unable to send ping message to %s@%s, hence removing.",
								serverThread.user.getUserName(), serverThread.getHostAddress()));
						iterator.remove();
					}
				}
				System.out.println("ServerThreadCleaner finished.");
			}
		}, 600, 600, TimeUnit.SECONDS);
	}
}
