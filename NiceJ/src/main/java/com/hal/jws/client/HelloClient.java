package com.hal.jws.client;


public class HelloClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
				HelloWorld_Service service = new HelloWorld_Service();
				HelloWorld hImpl = service.getHelloWorldImplPort();
				System.out.println(hImpl.sayHello("hal"));
	}

}
