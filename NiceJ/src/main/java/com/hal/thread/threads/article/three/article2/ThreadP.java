package com.hal.thread.threads.article.three.article2;

public class ThreadP extends Thread {
	private Producer producer;
	
	public ThreadP(Producer producer) {
		// TODO Auto-generated constructor stub
		super();
		this.producer = producer;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		producer.setValue();
	}
}
