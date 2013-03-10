package com.cb.oneclipboard.server.admin;

import com.cb.oneclipboard.server.Registery;
import com.cb.oneclipboard.server.ServerThread;

public class AdminCommandProcessor {

	public String processCommand(String command) {
		String output = "";
		if (command == null) {
			command = "";
		}

		switch (command) {
		case "client list":
			output = getFormattedClientList();
			break;
		case "client count":
			output = String.valueOf(getClientCount());
			break;
		case "exit":
			output = "exit";
			break;
		default:
			output = "Unknown command.";
		}

		return output;
	}

	private String getFormattedClientList() {
		String result = "Connected clients:";

		if (Registery.getClientSockets().isEmpty()) {
			result = "\nNone.";
		} else {
			for (ServerThread serverThread : Registery.getClientSockets()) {
				result = result + "\n" + serverThread.getHostAddress();
			}
		}

		return result;
	}

	private int getClientCount() {
		return Registery.getClientSockets().size();
	}
}
