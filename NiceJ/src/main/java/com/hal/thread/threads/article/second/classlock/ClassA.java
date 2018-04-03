package com.hal.thread.threads.article.second.classlock;

public class ClassA extends Thread {
	private ClassService service;
	public ClassA(ClassService service) {
		// TODO Auto-generated constructor stub
		this.service = service;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		service.printA();
	}
}
