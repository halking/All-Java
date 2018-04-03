package com.hal.thread.threads.article.second.classlock;

public class ClassB extends Thread {
	private ClassService service;
	public ClassB(ClassService service) {
		// TODO Auto-generated constructor stub
		this.service = service;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		service.printB();
	}
}
