package com.hal.thread.threads.article.second;

public class ServiceA extends Thread {
	private SynService service;
	public ServiceA(SynService service) {
		// TODO Auto-generated constructor stub
		this.service = service;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		service.doSomething("a", "aa");
	}
}
