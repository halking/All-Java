package com.hal.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author hal
 */
public class ServerDemo {

	/**
	 *2016年4月26日
	 * @param args
	 */
	public static void main(String[] args) {
      try {
		ServerSocket  server = null;
		try {
			server = new ServerSocket(8080);
			 
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("无法监听到该端口"+e);
		}
		Socket socket= null;
		try {
			socket = server.accept();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error accpet!"+e);
		}
		String line ="";
		BufferedReader bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		while (line!=null && !line.equals("bye")) {
			line=bReader.readLine();
			System.out.println("Server:"+line);
		}
		bReader.close();
		socket.close();
		server.close();
	} catch (Exception e) {
		System.out.println("Server Error:"+e);
	}
	}

}
