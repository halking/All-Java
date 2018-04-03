package com.hal.thread.threads.article.three.article2;

public class Producer {

	private String lock;
	
	public Producer(String lock) {
		super();
		this.lock =lock;
	}
	
	public void setValue() {
		try {
		synchronized (lock) {
			while (!ValueObject.value.equals("")) {
				System.out.println("producer "+Thread.currentThread().getName() + " Waiting!");
				lock.wait();
			}
			System.out.println("producer "+ Thread.currentThread().getName() + " is running!");
			String value = System.currentTimeMillis()+"_"+System.nanoTime();
			ValueObject.value = value;
			lock.notify();
		}
	} catch (InterruptedException e) {
		// TODO: handle exception
		e.printStackTrace();
	}
	}
}
