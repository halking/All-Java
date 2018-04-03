package com.hal.thread.threads.article.second.article3;

public class AtomicIntegerTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AtomicIntegerThread service = new AtomicIntegerThread();
		Thread t1 = new Thread(service);
		t1.start();
		Thread t2 = new Thread(service);
		t2.start();
		Thread t3 = new Thread(service);
		t3.start();
		Thread t4 = new Thread(service);
		t4.start();
	}

}
