package com.hal.socket;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class SockedDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		   try {
			Socket  socket = new Socket();
			socket.connect(new InetSocketAddress("111.13.100.92", 80),5000);
			try {
				InputStream is = socket.getInputStream();
				Scanner  scanner = new Scanner(is);
				while (scanner.hasNextLine()) {
					String  line  = scanner.nextLine();
					System.out.println(line);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}finally{
				socket.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
