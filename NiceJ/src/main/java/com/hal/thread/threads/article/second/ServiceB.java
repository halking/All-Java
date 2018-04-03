package com.hal.thread.threads.article.second;

public class ServiceB extends Thread {
	private SynService service;
	public ServiceB(SynService service) {
		// TODO Auto-generated constructor stub
		this.service = service;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		service.doAntherthing();
	}
}
