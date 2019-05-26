package org.bitbucket.socket_thing_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public final class GodWtfJustWorkMain {
	public static void main(String[] args) {
		try {
			InetAddress localHost = InetAddress.getByName("127.0.0.1");
			ServerSocket server = new ServerSocket(59001, 10, localHost);
			Socket clientSocket = server.accept();
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			while (true) {
				String line;
				if ((line = in.readLine()) != null) {
					System.out.println(line);
				}
				try {
					Thread.sleep(50L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//in.close();
			//server.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
