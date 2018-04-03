package com.hal.jws.service;

import javax.xml.ws.Endpoint;

import com.hal.jws.HelloWorldImpl;

public class JMSWebService {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
				System.out.println("start server...");
				HelloWorldImpl  hImpl = new  HelloWorldImpl();
				String address = "http://localhost:8443/helloWorld";
				Endpoint.publish(address, hImpl);
				System.out.println("webservice statring...");
	}

}
