package com.hal.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author hal
 */
public class Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Socket socket = new Socket("127.0.0.1", 8080);
			socket.setSoTimeout(5000);
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter os = new PrintWriter(socket.getOutputStream());
			String inline;
			inline = br.readLine();
			while (!inline.equals("bye")) {
				os.println(inline);
				os.flush();
				inline = br.readLine();
			}
			os.close();
			socket.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error：" + e);
		}
	}

}
