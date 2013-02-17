package com.cb.oneclipboard.server;

import java.util.ArrayList;
import java.util.List;

public class Registery {
	private static List<String> clients = new ArrayList<>();
	
	public static void register(String ip){
		clients.add(ip);
	}
	
	public static List<String> getClients(){
		return clients;
	}
}
