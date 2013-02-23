package com.cb.oneclipboard.server;

import java.util.ArrayList;
import java.util.List;

public class Registery {
	private static List<ClientData> clients = new ArrayList<>();
	
	public static void register(String ip, int port){
		System.out.println("Registering " + ip + ":" + port);
		clients.add(new ClientData(ip, port));
	}
	
	public static List<ClientData> getClients(){
		return clients;
	}
}
