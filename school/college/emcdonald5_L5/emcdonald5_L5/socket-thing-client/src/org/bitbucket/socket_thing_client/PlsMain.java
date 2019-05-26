package org.bitbucket.socket_thing_client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public final class PlsMain {
	public static void main(String[] args) {
		try {
			Socket clientSocket = new Socket(InetAddress.getByName("127.0.0.1"), 59001);
			if (clientSocket.isConnected()) {
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
				out.println("emcdonald5");
				// Flushes the stream so that it actually gets sent:
				out.flush();
				// Keeps the connection open:
				while (true) {
					try {
						Thread.sleep(50L);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
