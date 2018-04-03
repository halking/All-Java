package com.hal.thread.threads.article.second.classlock;

public class ClassC extends Thread {
	private ClassService service;
	public ClassC(ClassService service) {
		// TODO Auto-generated constructor stub
		this.service = service;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		service.printC();
	}
}
