package com.hal.thread.threads.article.three.article2;

public class Consumer {
	private String lock;

	public Consumer(String lock) {
		super();
		this.lock =lock;
	}

	public void getValue() {
		try {
			synchronized (lock) {
				while (ValueObject.value.equals("")) {
					System.out.println("consumer " + Thread.currentThread().getName() + " Waiting!");
					lock.wait();
				}
				System.out.println("consumer " + Thread.currentThread().getName() + " is running!");
				ValueObject.value = "";
				lock.notify();
			}
		} catch (InterruptedException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
