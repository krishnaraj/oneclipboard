package com.cb.oneclipboard.server;

import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ServerThreadCleaner {
	private final static Logger LOGGER = Logger.getLogger(ServerThreadCleaner.class.getName());

	private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public static void start(String args[]) throws Exception {
		final ScheduledFuture<?> scheduledCleaner = scheduler.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				LOGGER.info("ServerThreadCleaner running...");
				Iterator<ServerThread> iterator = Registery.getClientSockets().iterator();
				while (iterator.hasNext()) {
					ServerThread serverThread = iterator.next();
					try {
						LOGGER.info("Pinging " + serverThread.user.getUserName() + "@" + serverThread.getHostAddress());
						serverThread.ping();
					} catch (Exception e) {
						LOGGER.severe(String.format("Unable to send ping message to %s@%s, hence removing.",
								serverThread.user.getUserName(), serverThread.getHostAddress()));
						serverThread.close();
						iterator.remove();
					}
				}
				LOGGER.info("ServerThreadCleaner finished.");
			}
		}, 4, 4, TimeUnit.HOURS);
	}
}
