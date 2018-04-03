package com.hal.thread.threads.article.second;

public class ServiceMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SynService service = new SynService();
		ServiceA serviceA = new ServiceA(service);
		serviceA.setName("serviceA");
		ServiceB serviceB = new ServiceB(service);
		serviceB.setName("serviceB");
		serviceA.start();
		serviceB.start();
	}

}
