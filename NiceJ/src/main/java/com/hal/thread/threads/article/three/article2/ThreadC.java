package com.hal.thread.threads.article.three.article2;

public class ThreadC extends Thread {
	private Consumer consumer;
	
	public ThreadC(Consumer consumer) {
		// TODO Auto-generated constructor stub
		super();
		this.consumer = consumer;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		consumer.getValue();
	}
}
